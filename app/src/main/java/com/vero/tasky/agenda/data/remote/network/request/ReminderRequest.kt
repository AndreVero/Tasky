package com.vero.tasky.agenda.data.remote.network.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReminderRequest(
    val id: String,
    val title: String,
    val description: String?,
    val remindAt: Long,
    val time: Long,
)