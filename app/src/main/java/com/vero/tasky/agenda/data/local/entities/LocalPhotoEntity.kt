package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vero.tasky.agenda.domain.model.AgendaPhoto

@Entity
data class LocalPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val eventId: String,
    val path: String,
)

fun LocalPhotoEntity.toLocalPhoto() : AgendaPhoto.LocalPhoto {
    return AgendaPhoto.LocalPhoto(
        uri = path
    )
}