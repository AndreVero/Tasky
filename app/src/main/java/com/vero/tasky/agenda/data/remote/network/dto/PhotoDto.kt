package com.vero.tasky.agenda.data.remote.network.dto

import com.squareup.moshi.JsonClass
import com.vero.tasky.agenda.data.local.entities.NetworkPhotoEntity

@JsonClass(generateAdapter = true)
data class PhotoDto (
    val key: String,
    val url: String,
)

fun PhotoDto.toPhotoEntity(eventId: String) : NetworkPhotoEntity {
    return NetworkPhotoEntity(
        key = key,
        path = url,
        eventId = eventId
    )
}