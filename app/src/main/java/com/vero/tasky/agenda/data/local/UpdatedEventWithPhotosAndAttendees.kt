package com.vero.tasky.agenda.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.vero.tasky.agenda.data.local.entities.*

data class UpdatedEventWithPhotosAndAttendees(
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
    val attendees: List<CreatedAttendeeEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val deletedPhotosKeys: List<DeletedLocalPhotoEntity>
)