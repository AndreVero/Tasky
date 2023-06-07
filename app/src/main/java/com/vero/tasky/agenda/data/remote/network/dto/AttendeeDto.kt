package com.vero.tasky.agenda.data.remote.network.dto

import com.squareup.moshi.JsonClass
import com.vero.tasky.agenda.data.local.entities.AttendeeEntity

@JsonClass(generateAdapter = true)
data class AttendeeDto (
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long,
)

fun AttendeeDto.toAttendeeEntity() : AttendeeEntity {
    return AttendeeEntity(
        userId = userId,
        email = email,
        fullName = fullName,
        eventId = eventId,
        isGoing = isGoing,
        remindAt = remindAt
    )
}
