package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vero.tasky.agenda.domain.model.AgendaPhoto

@Entity
data class NetworkPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val eventId: String,
    val key: String,
    val path: String,
)

fun NetworkPhotoEntity.toNetworkPhoto() : AgendaPhoto.RemotePhoto {
    return AgendaPhoto.RemotePhoto(
        key = key,
        url = path
    )
}