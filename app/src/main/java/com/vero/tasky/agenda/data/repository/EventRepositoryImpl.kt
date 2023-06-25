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
import com.vero.tasky.agenda.domain.repository.EventRepository
import com.vero.tasky.agenda.domain.workmanagerrunner.CreateEventWorkerRunner
import com.vero.tasky.agenda.domain.workmanagerrunner.UpdateEventWorkerRunner
import com.vero.tasky.core.data.remote.safeSuspendCall
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventRepositoryImpl(
    private val api: EventApi,
    private val eventDao: EventDao,
    private val modifiedAgendaItemDao: ModifiedAgendaItemDao,
    private val multipartParser: MultipartParser,
    private val createEventWorkerRunner: CreateEventWorkerRunner,
    private val updateEventWorkerRunner: UpdateEventWorkerRunner
) : EventRepository {

    override suspend fun createEvent(
        event: AgendaItem.Event,
    ): Result<AgendaItemUploadResult> {
        val localPhotos = event.photos.filterIsInstance<AgendaPhoto.LocalPhoto>()

        saveEvent(event = event, localPhotos = localPhotos, deletedPhotoKeys = emptyList())

        val multipartPhotos = multipartParser.getMultipartPhotos(localPhotos)
        val skippedPhoto = localPhotos.size - multipartPhotos.size

        createEventWorkerRunner.run(event.id)

        return Result.success(AgendaItemUploadResult(skippedPhoto))
    }

    private suspend fun saveEvent(
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
        return eventDao.loadEventFlow(id).map { it.toEvent() }
    }


    override suspend fun updateEvent(
        event: AgendaItem.Event,
        deletedPhotoKeys: List<String>,
        isGoing: Boolean
    ): Result<AgendaItemUploadResult> {
        val localPhotos = event.photos.filterIsInstance<AgendaPhoto.LocalPhoto>()

        saveEvent(event = event, localPhotos = localPhotos, deletedPhotoKeys = deletedPhotoKeys)

        val multipartPhotos = multipartParser.getMultipartPhotos(localPhotos)
        val skippedPhoto = localPhotos.size - multipartPhotos.size

        updateEventWorkerRunner.run(isGoing, event.id)

        return Result.success(AgendaItemUploadResult(skippedPhoto))
    }

    override suspend fun fetchEvent(eventId: String){
        safeSuspendCall {
            val result = api.fetchEvent(eventId = eventId)
            eventDao.insertEvents(result.toEventEntity())
        }
    }

    override suspend fun deleteEvent(event: AgendaItem.Event) {
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