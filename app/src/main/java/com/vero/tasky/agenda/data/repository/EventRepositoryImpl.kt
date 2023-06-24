package com.vero.tasky.agenda.data.repository

import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.local.entities.ModifiedAgendaItemEntity
import com.vero.tasky.agenda.data.mappers.*
import com.vero.tasky.agenda.data.remote.network.api.EventApi
import com.vero.tasky.agenda.data.remote.network.dto.toAttendeeBaseInfo
import com.vero.tasky.agenda.data.util.multipart.MultipartParser
import com.vero.tasky.agenda.domain.model.*
import com.vero.tasky.agenda.domain.repository.EventRepository
import com.vero.tasky.core.data.remote.safeSuspendCall
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventRepositoryImpl(
    private val api: EventApi,
    private val eventDao: EventDao,
    private val modifiedAgendaItemDao: ModifiedAgendaItemDao,
    private val multipartParser: MultipartParser
) : EventRepository {

    override suspend fun createEvent(
        event: AgendaItem.Event,
    ): Result<AgendaItemUploadResult> {
        val localPhotos = event.photos.filterIsInstance<AgendaPhoto.LocalPhoto>()

        saveEvent(event = event, localPhotos = localPhotos)

        var skippedPhoto = 0

        safeSuspendCall {
            withContext(Dispatchers.IO) {
                val multipartPhotos = multipartParser.getMultipartPhotos(localPhotos)
                skippedPhoto = localPhotos.size - multipartPhotos.size
                api.createEvent(
                    id = multipartParser.getMultipartId(event.id),
                    title = multipartParser.getMultipartTitle(event.title),
                    description = multipartParser.getMultipartDescription(event.description),
                    from = multipartParser.getMultipartFrom(event.time),
                    to = multipartParser.getMultipartTo(event.to),
                    remindAt = multipartParser.getMultipartRemindAt(event.remindAt),
                    attendeeIds = multipartParser.getMultipartAttendeesIds(event.attendees),
                    photos = multipartPhotos
                )
            }
        }.onFailure {
            modifiedAgendaItemDao.insertAgendaItem(
                ModifiedAgendaItemEntity(
                    id = event.id,
                    agendaItemType = AgendaItemType.EVENT,
                    modificationType = ModificationType.CREATED
                )
            )
        }
        return Result.success(AgendaItemUploadResult(skippedPhoto))
    }

    private suspend fun saveEvent(
        event: AgendaItem.Event,
        localPhotos: List<AgendaPhoto.LocalPhoto>
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
                (attendeeJobs + photoJobs).joinAll()
            }
        }
    }

    override fun getEvent(id: String): Flow<AgendaItem.Event> {
        return eventDao.loadEvent(id).map { it.toEvent() }
    }


    override suspend fun updateEvent(
        event: AgendaItem.Event,
        deletedPhotoKeys: List<String>,
        isGoing: Boolean
    ) : Result<AgendaItemUploadResult>{
        val localPhotos = event.photos.filterIsInstance<AgendaPhoto.LocalPhoto>()

        saveEvent(event = event, localPhotos = localPhotos)

        var skippedPhoto = 0

        safeSuspendCall {
            withContext(Dispatchers.IO) {
                val multipartPhotos = multipartParser.getMultipartPhotos(localPhotos)
                skippedPhoto = localPhotos.size - multipartPhotos.size
                api.updateEvent(
                    id = multipartParser.getMultipartId(event.id),
                    title = multipartParser.getMultipartTitle(event.title),
                    description = multipartParser.getMultipartDescription(event.description),
                    from = multipartParser.getMultipartFrom(event.time),
                    to = multipartParser.getMultipartTo(event.to),
                    remindAt = multipartParser.getMultipartRemindAt(event.remindAt),
                    attendeeIds = multipartParser.getMultipartAttendeesIds(event.attendees),
                    photos = multipartPhotos,
                    isGoing = multipartParser.getMultipartIsGoing(isGoing),
                    deletedPhotoKeys = multipartParser.getMultipartDeletedPhotosKeys(deletedPhotoKeys)
                )
            }
        }.onFailure {
            modifiedAgendaItemDao.insertAgendaItem(
                ModifiedAgendaItemEntity(
                    id = event.id,
                    agendaItemType = AgendaItemType.EVENT,
                    modificationType = ModificationType.UPDATED
                )
            )
        }
        return Result.success(AgendaItemUploadResult(skippedPhoto))
    }

    override suspend fun fetchEvent(eventId: String): Result<Unit> {
        return safeSuspendCall {
            val result = api.fetchEvent(eventId = eventId)
            eventDao.insertEvents(result.toEventEntity())
        }
    }

    override suspend fun deleteEvent(event: AgendaItem.Event){
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