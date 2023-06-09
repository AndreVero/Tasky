package com.vero.tasky.agenda.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.vero.tasky.agenda.data.local.entities.*

data class EventWithPhotosAndAttendees(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val networkPhotos: List<RemotePhotoEntity>,
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