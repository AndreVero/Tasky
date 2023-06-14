package com.vero.tasky.agenda.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.vero.tasky.agenda.data.local.entities.*

data class CreatedEventWithPhotosAndAttendees(
    @Embedded val event: CreatedEventEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val localPhotos: List<CreatedLocalPhotoEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val attendees: List<CreatedAttendeeEntity>
)