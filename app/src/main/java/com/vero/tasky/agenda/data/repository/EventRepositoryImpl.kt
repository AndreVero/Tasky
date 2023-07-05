package com.vero.tasky.agenda.data.repository

import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.local.entities.DeletedPhotoEntity
import com.vero.tasky.agenda.data.local.entities.ModifiedAgendaItemEntity
import com.vero.tasky.agenda.data.mappers.*
import com.vero.tasky.agenda.data.remote.network.api.EventApi
import com.vero.tasky.agenda.data.remote.network.dto.toAttendeeBaseInfo
import com.vero.tasky.agenda.data.util.multipart.MultipartParser
import com.vero.tasky.agenda.domain.model.*
import com.vero.tasky.agenda.domain.remindermanager.AlarmData
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.agenda.domain.repository.EventRepository
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveEventWorkerRunner
import com.vero.tasky.core.data.remote.safeSuspendCall
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class EventRepositoryImpl(
    private val api: EventApi,
    private val eventDao: EventDao,
    private val modifiedAgendaItemDao: ModifiedAgendaItemDao,
    private val multipartParser: MultipartParser,
    private val saveEventWorkerRunner: SaveEventWorkerRunner,
    private val alarmHandler: AlarmHandler,
) : EventRepository {

    override suspend fun saveEvent(
        event: AgendaItem.Event,
        isGoing: Boolean,
        deletedPhotoKeys: List<String>,
        modificationType: ModificationType
    ): Result<AgendaItemUploadResult> {
        val localPhotos = event.photos.filterIsInstance<AgendaPhoto.LocalPhoto>()
        alarmHandler.setAlarm(
            AlarmData(
                time = event.remindAt,
                itemId = event.id,
                title = event.title,
                description = event.description
            )
        )
        saveEventLocally(
            event = event,
            localPhotos = localPhotos,
            deletedPhotoKeys = deletedPhotoKeys
        )

        val multipartPhotos = multipartParser.getMultipartPhotos(localPhotos)
        val skippedPhotos = localPhotos.size - multipartPhotos.size

        saveEventWorkerRunner.run(
            isGoing = isGoing,
            modificationType = modificationType,
            eventId = event.id
        )

        return Result.success(AgendaItemUploadResult(skippedPhotos))
    }

    private suspend fun saveEventLocally(
        event: AgendaItem.Event,
        localPhotos: List<AgendaPhoto.LocalPhoto>,
        deletedPhotoKeys: List<String>
    ) {
        safeSuspendCall {
            coroutineScope {
                eventDao.insertEvents(event.toEventEntity())
                val attendeeJobs = event.attendees.map {
                    launch { eventDao.insertAttendees(it.toAttendeeEntity()) }
                }
                val photoJobs = localPhotos.map {
                    launch { eventDao.insertLocalPhotoEntity(it.toLocalPhotoEntity(event.id)) }
                }
                val deletedPhotoJobs = deletedPhotoKeys.map { key ->
                    launch {
                        eventDao.insertDeletedPhotoEntity(
                            DeletedPhotoEntity(
                                eventId = event.id,
                                key = key
                            )
                        )
                    }
                }
                (attendeeJobs + photoJobs + deletedPhotoJobs).joinAll()
            }
        }
    }

    override fun getEvent(id: String): Flow<AgendaItem.Event> {
        return eventDao.loadEventFlow(id).mapNotNull { it?.toEvent() }
    }

    override suspend fun fetchEvent(eventId: String){
        safeSuspendCall {
            val result = api.fetchEvent(eventId = eventId)
            eventDao.insertEvents(result.toEventEntity())
        }
    }

    override suspend fun deleteEvent(event: AgendaItem.Event) {
        alarmHandler.cancelAlarm(event.id)
        eventDao.deleteEvents(event.toEventEntity())

        safeSuspendCall {
            api.deleteEvent(eventId = event.id)
        }.onFailure {
            modifiedAgendaItemDao.insertAgendaItem(
                ModifiedAgendaItemEntity(
                    id = event.id,
                    agendaItemType = AgendaItemType.EVENT,
                    modificationType = ModificationType.DELETED
                )
            )
        }
    }

    override suspend fun getAttendee(email: String): Result<AttendeeBaseInfo?> {
        return safeSuspendCall {
            val result = api.getAttendee(email)
            if (result.doesUserExist)
                result.attendee.toAttendeeBaseInfo()
            else
                null
        }
    }

    override suspend fun deleteAttendee(eventId: String): Result<Unit> {
        return safeSuspendCall {
            api.deleteAttendee(eventId)
        }
    }

}