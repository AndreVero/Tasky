package com.vero.tasky.agenda.domain.repository

import com.vero.tasky.agenda.domain.model.*
import kotlinx.coroutines.flow.Flow

interface EventRepository {

    suspend fun createEvent(
        event: AgendaItem.Event
    ) : Result<AgendaItemUploadResult>

    fun getEvent(id: String): Flow<AgendaItem.Event>

    suspend fun updateEvent(
        event: AgendaItem.Event,
        deletedPhotoKeys: List<String>,
        isGoing: Boolean
    ) : Result<AgendaItemUploadResult>

    suspend fun fetchEvent(eventId: String) : Result<Unit>

    suspend fun deleteEvent(event: AgendaItem.Event)

    suspend fun getAttendee(email: String) : Result<AttendeeBaseInfo?>

    suspend fun deleteAttendee(eventId: String) : Result<Unit>
}