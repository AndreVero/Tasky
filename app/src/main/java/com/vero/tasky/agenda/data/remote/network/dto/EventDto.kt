package com.vero.tasky.agenda.data.remote.network.dto

import com.squareup.moshi.JsonClass
import com.vero.tasky.agenda.data.local.entities.EventEntity

@JsonClass(generateAdapter = true)
data class EventDto(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: List<AttendeeDto>,
    val photos: List<PhotoDto>
)

fun EventDto.toEventEntity() : EventEntity {
    return EventEntity(
        id = id,
        title = title,
        description = description,
        time = from,
        to = to,
        remindAt = remindAt,
        host = host,
        isUserEventCreator = isUserEventCreator,
        attendees = attendees.map { it.toAttendeeEntity() },
        networkPhotos = photos.map { it.toPhotoEntity(id) },
        localPhotos = emptyList()
    )
}