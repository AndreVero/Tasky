package com.vero.tasky.agenda.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.vero.tasky.agenda.data.local.entities.*
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaPhoto

data class EventWithPhotosAndAttendees(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val networkPhotos: List<NetworkPhotoEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val localPhotos: List<LocalPhotoEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val attendees: List<AttendeeEntity>
)

fun EventWithPhotosAndAttendees.toEvent() : AgendaItem.Event {

    val photos = mutableListOf<AgendaPhoto>()
    photos.addAll(event.networkPhotos.map { it.toNetworkPhoto() })
    photos.addAll(event.localPhotos.map { it.toLocalPhoto() })

    return AgendaItem.Event (
        id = event.id,
        title = event.title,
        description = event.description,
        remindAt = LocalDateTimeConverter.longToLocalDateTime(event.remindAt),
        time = LocalDateTimeConverter.longToLocalDateTime(event.time),
        to = LocalDateTimeConverter.longToLocalDateTime(event.to),
        host = event.host,
        isUserEventCreator = event.isUserEventCreator,
        attendees = attendees.map { it.toAttendee() },
        photos = photos
    )

}