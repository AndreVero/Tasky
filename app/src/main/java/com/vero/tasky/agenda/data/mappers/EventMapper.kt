package com.vero.tasky.agenda.data.mappers

import com.vero.tasky.agenda.data.local.EventWithPhotosAndAttendees
import com.vero.tasky.agenda.data.local.entities.EventEntity
import com.vero.tasky.agenda.data.remote.network.dto.EventDto
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaPhoto

object EventMapper {

    fun toEventEntity(eventDto: EventDto) : EventEntity {
        return EventEntity(
            id = eventDto.id,
            title = eventDto.title,
            description = eventDto.description,
            time = eventDto.from,
            to = eventDto.to,
            remindAt = eventDto.remindAt,
            host = eventDto.host,
            isUserEventCreator = eventDto.isUserEventCreator,
            attendees = eventDto.attendees.map { AttendeeMapper.toAttendeeEntity(it) },
            networkPhotos = eventDto.photos.map { PhotoMapper.toPhotoEntity(eventDto.id, it) },
            localPhotos = emptyList()
        )
    }

    fun toEvent(item: EventWithPhotosAndAttendees) : AgendaItem.Event {
        val event = item.event
        val photos = mutableListOf<AgendaPhoto>()
        photos.addAll(event.networkPhotos.map { PhotoMapper.toNetworkPhoto(it) })
        photos.addAll(event.localPhotos.map { PhotoMapper.toLocalPhoto(it) })

        return AgendaItem.Event (
            id = event.id,
            title = event.title,
            description = event.description,
            remindAt = LocalDateTimeConverter.longToLocalDateTime(event.remindAt),
            time = LocalDateTimeConverter.longToLocalDateTime(event.time),
            to = LocalDateTimeConverter.longToLocalDateTime(event.to),
            host = event.host,
            isUserEventCreator = event.isUserEventCreator,
            attendees = item.attendees.map { AttendeeMapper.toAttendee(it) },
            photos = photos
        )

    }

}