package com.vero.tasky.agenda.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.vero.tasky.agenda.data.local.entities.AttendeeEntity
import com.vero.tasky.agenda.data.local.entities.EventEntity
import com.vero.tasky.agenda.data.local.entities.PhotoEntity

data class EventWithPhotosAndAttendees(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val photos: List<PhotoEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val attendee: List<AttendeeEntity>
)