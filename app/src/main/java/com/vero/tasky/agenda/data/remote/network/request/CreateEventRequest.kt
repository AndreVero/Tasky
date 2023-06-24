package com.vero.tasky.agenda.data.remote.network.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateEventRequest(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val attendeesIds: List<String>
)