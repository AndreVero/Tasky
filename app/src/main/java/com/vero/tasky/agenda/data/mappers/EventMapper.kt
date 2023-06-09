package com.vero.tasky.agenda.data.mappers

import com.vero.tasky.agenda.data.local.EventWithPhotosAndAttendees
import com.vero.tasky.agenda.data.local.entities.EventEntity
import com.vero.tasky.agenda.data.remote.network.dto.EventDto
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
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
    photos.addAll(this.networkPhotos.map { it.toNetworkPhoto() })
    photos.addAll(this.localPhotos.map { it.toLocalPhoto() })

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