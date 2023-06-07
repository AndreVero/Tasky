package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vero.tasky.agenda.domain.model.Attendee

@Entity
data class AttendeeEntity(
    @PrimaryKey(autoGenerate = false) val userId: String,
    val email: String,
    val fullName: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long,
)

fun AttendeeEntity.toAttendee() : Attendee {
    return Attendee(
        email = email,
        fullName = fullName,
        eventId = eventId,
        isGoing = isGoing,
        remindAt = remindAt,
        userId = userId
    )
}