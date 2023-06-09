package com.vero.tasky.agenda.data.mappers

import com.vero.tasky.agenda.data.local.entities.AttendeeEntity
import com.vero.tasky.agenda.data.remote.network.dto.AttendeeDto
import com.vero.tasky.agenda.domain.model.Attendee

fun AttendeeDto.toAttendeeEntity() : AttendeeEntity {
    return AttendeeEntity(
        userId = this.userId,
        email = this.email,
        fullName = this.fullName,
        eventId = this.eventId,
        isGoing = this.isGoing,
        remindAt = this.remindAt
    )
}
fun AttendeeEntity.toAttendee() : Attendee {
    return Attendee(
        email = this.email,
        fullName = this.fullName,
        eventId = this.eventId,
        isGoing = this.isGoing,
        remindAt = this.remindAt,
        userId = this.userId
    )
}
