package com.vero.tasky.agenda.data.mappers

import com.vero.tasky.agenda.data.local.entities.AttendeeEntity
import com.vero.tasky.agenda.data.remote.network.dto.AttendeeDto
import com.vero.tasky.agenda.domain.model.Attendee

object AttendeeMapper {

    fun toAttendeeEntity(attendeeDto: AttendeeDto) : AttendeeEntity {
        return AttendeeEntity(
            userId = attendeeDto.userId,
            email = attendeeDto.email,
            fullName = attendeeDto.fullName,
            eventId = attendeeDto.eventId,
            isGoing = attendeeDto.isGoing,
            remindAt = attendeeDto.remindAt
        )
    }

    fun toAttendee(attendeeEntity: AttendeeEntity) : Attendee {
        return Attendee(
            email = attendeeEntity.email,
            fullName = attendeeEntity.fullName,
            eventId = attendeeEntity.eventId,
            isGoing = attendeeEntity.isGoing,
            remindAt = attendeeEntity.remindAt,
            userId = attendeeEntity.userId
        )
    }
}