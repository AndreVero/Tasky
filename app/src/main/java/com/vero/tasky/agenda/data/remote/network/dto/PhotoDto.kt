package com.vero.tasky.agenda.data.remote.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoDto (
    val key: String,
    val url: String,
)