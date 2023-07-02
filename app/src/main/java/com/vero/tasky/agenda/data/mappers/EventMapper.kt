package com.vero.tasky.agenda.data.mappers

import com.vero.tasky.agenda.data.local.EventWithPhotosAndAttendees
import com.vero.tasky.agenda.data.local.entities.AttendeeEntity
import com.vero.tasky.agenda.data.local.entities.EventEntity
import com.vero.tasky.agenda.data.remote.network.dto.EventDto
import com.vero.tasky.agenda.data.remote.network.request.CreateEventRequest
import com.vero.tasky.agenda.data.remote.network.request.UpdateEventRequest
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter.localDateTimeToLong
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaPhoto

fun EventDto.toEventEntity(): EventEntity {
    return EventEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.from,
        to = this.to,
        remindAt = this.remindAt,
        host = this.host,
        isUserEventCreator = this.isUserEventCreator
    )
}

fun EventWithPhotosAndAttendees.toEvent(): AgendaItem.Event {
    val event = this.event
    val photos = mutableListOf<AgendaPhoto>()
    photos.addAll(this.networkPhotos.map { it.toRemotePhoto() })

    return AgendaItem.Event(
        id = event.id,
        title = event.title,
        description = event.description,
        remindAt = LocalDateTimeConverter.longToLocalDateTime(event.remindAt),
        time = LocalDateTimeConverter.longToLocalDateTime(event.time),
        to = LocalDateTimeConverter.longToLocalDateTime(event.to),
        host = event.host,
        isUserEventCreator = event.isUserEventCreator,
        attendees = this.attendees.map { it.toAttendee() },
        photos = photos
    )
}

fun AgendaItem.Event.toEventEntity(): EventEntity {
    return EventEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        time = localDateTimeToLong(this.time),
        to = localDateTimeToLong(this.to),
        remindAt = localDateTimeToLong(this.remindAt),
        host = this.host,
        isUserEventCreator = this.isUserEventCreator
    )
}

fun EventEntity.toCreateEventRequest(attendeesEntity: List<AttendeeEntity>) : CreateEventRequest {
    return CreateEventRequest(
        id = this.id,
        title = this.title,
        description = this.description ?: "",
        from = this.time,
        to = this.to,
        remindAt = this.remindAt,
        attendeeIds = attendeesEntity.map { it.userId }
    )
}

fun EventEntity.toUpdateEventRequest(
    isGoing: Boolean,
    deletedPhotoKeys: List<String>,
    attendeesEntity: List<AttendeeEntity>
) : UpdateEventRequest {
    return UpdateEventRequest(
        id = this.id,
        title = this.title,
        description = this.description ?: "",
        from = this.time,
        to = this.to,
        remindAt = this.remindAt,
        attendeeIds = attendeesEntity.map { it.userId },
        isGoing = isGoing,
        deletedPhotoKeys = deletedPhotoKeys
    )
}