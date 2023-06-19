package com.vero.tasky.agenda.data.local.dao

import com.vero.tasky.agenda.data.local.EventWithPhotosAndAttendees
import com.vero.tasky.agenda.data.local.entities.AttendeeEntity
import com.vero.tasky.agenda.data.local.entities.EventEntity
import com.vero.tasky.agenda.data.local.entities.RemotePhotoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventDaoFake : EventDao {

    private val eventList = mutableSetOf<EventWithPhotosAndAttendees>()

    override suspend fun insertEvents(vararg events: EventEntity) {
        events.forEach {
            eventList.add(
                EventWithPhotosAndAttendees(
                    event = it,
                    attendees = emptyList(),
                    localPhotos = emptyList(),
                    networkPhotos = emptyList()
                )
            )
        }
    }

    override suspend fun insertAttendees(vararg events: AttendeeEntity) {}

    override suspend fun insertRemotePhotoEntity(vararg events: RemotePhotoEntity) {}

    override suspend fun updateEvents(vararg events: EventEntity) {}

    override suspend fun deleteEvents(vararg events: EventEntity) {}

    override fun loadEventsForDay(timestamp: Long): Flow<List<EventWithPhotosAndAttendees>> {
        return flow { emit(eventList.toList()) }
    }
}