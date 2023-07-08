package com.vero.tasky.agenda.data.local.dao

import com.vero.tasky.agenda.data.local.EventWithPhotosAndAttendees
import com.vero.tasky.agenda.data.local.entities.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EventDaoFake : EventDao {

    private val eventSet = mutableSetOf<EventEntity>()
    private val attendeeSet = mutableSetOf<AttendeeEntity>()
    private val remotePhotoSet = mutableSetOf<RemotePhotoEntity>()

    override suspend fun insertEvents(vararg events: EventEntity) {
        events.forEach {
            eventSet.add(it)
        }
    }

    override suspend fun insertAttendees(vararg attendees: AttendeeEntity) {
        attendees.forEach {
            attendeeSet.add(it)
        }
    }

    override suspend fun insertRemotePhotoEntity(vararg photos: RemotePhotoEntity) {
        photos.forEach {
            remotePhotoSet.add(it)
        }
    }
    override suspend fun insertLocalPhotoEntity(vararg photos: LocalPhotoEntity) {}
    override suspend fun insertDeletedPhotoEntity(vararg photos: DeletedPhotoEntity) {

    }

    override suspend fun updateEvents(vararg events: EventEntity) {}

    override suspend fun deleteEvents(vararg events: EventEntity) {}
    override suspend fun deleteLocalPhotos(vararg localPhotos: LocalPhotoEntity) {
    }

    override fun loadEventsForDay(from: Long, to: Long): Flow<List<EventWithPhotosAndAttendees>> {

        return flowOf(
            listOf(EventWithPhotosAndAttendees(
                event = eventSet.first(),
                attendees = attendeeSet.toList(),
                networkPhotos = remotePhotoSet.toList(),
                localPhotos = emptyList(),
                deletedPhoto = emptyList()
            ))
        )
    }

    override fun loadEventFlow(id: String): Flow<EventWithPhotosAndAttendees?> {
        return  flowOf(
            EventWithPhotosAndAttendees(
                event = eventSet.first(),
                attendees = attendeeSet.toList(),
                networkPhotos = remotePhotoSet.toList(),
                localPhotos = emptyList(),
                deletedPhoto = emptyList()
            )
        )
    }

    override suspend fun loadEvent(id: String): EventWithPhotosAndAttendees {
        return EventWithPhotosAndAttendees(
            event = eventSet.first(),
            attendees = attendeeSet.toList(),
            networkPhotos = remotePhotoSet.toList(),
            localPhotos = emptyList(),
            deletedPhoto = emptyList()
        )
    }

    override suspend fun loadAllEvents(): List<EventWithPhotosAndAttendees> {
        return listOf(
            EventWithPhotosAndAttendees(
                event = eventSet.first(),
                attendees = attendeeSet.toList(),
                networkPhotos = remotePhotoSet.toList(),
                localPhotos = emptyList(),
                deletedPhoto = emptyList()
            )
        )
    }
}