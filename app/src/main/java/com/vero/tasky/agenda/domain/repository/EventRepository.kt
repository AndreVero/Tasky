package com.vero.tasky.agenda.domain.repository

import com.vero.tasky.agenda.domain.model.*
import kotlinx.coroutines.flow.Flow

interface EventRepository {

    suspend fun saveEvent(
        event: AgendaItem.Event,
        isGoing: Boolean,
        deletedPhotoKeys: List<String>,
        modificationType: ModificationType
    ) : Result<AgendaItemUploadResult>

    fun getEvent(id: String): Flow<AgendaItem.Event>

    suspend fun fetchEvent(eventId: String)

    suspend fun deleteEvent(event: AgendaItem.Event)

    suspend fun getAttendee(email: String) : Result<AttendeeBaseInfo?>

    suspend fun deleteAttendee(eventId: String) : Result<Unit>
}