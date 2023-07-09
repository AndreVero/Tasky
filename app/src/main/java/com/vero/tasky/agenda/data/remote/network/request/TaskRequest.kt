package com.vero.tasky.agenda.data.remote.network.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TaskRequest(
    val id: String,
    val title: String,
    val description: String?,
    val remindAt: Long,
    val time: Long,
    val isDone: Boolean
)