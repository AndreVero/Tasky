package com.vero.tasky.agenda.data.local.dao

import com.vero.tasky.agenda.data.local.EventWithPhotosAndAttendees
import com.vero.tasky.agenda.data.local.entities.AttendeeEntity
import com.vero.tasky.agenda.data.local.entities.EventEntity
import com.vero.tasky.agenda.data.local.entities.LocalPhotoEntity
import com.vero.tasky.agenda.data.local.entities.RemotePhotoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

    override suspend fun updateEvents(vararg events: EventEntity) {}

    override suspend fun deleteEvents(vararg events: EventEntity) {}

    override fun loadEventsForDay(timestamp: Long): Flow<List<EventWithPhotosAndAttendees>> {

        return flow { emit(
            listOf(EventWithPhotosAndAttendees(
                event = eventSet.first(),
                attendees = attendeeSet.toList(),
                networkPhotos = remotePhotoSet.toList(),
                localPhotos = emptyList()
            ))
        ) }
    }
}