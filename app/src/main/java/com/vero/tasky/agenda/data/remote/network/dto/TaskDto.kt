package com.vero.tasky.agenda.data.remote.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TaskDto (
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
    val isDone: Boolean
    )