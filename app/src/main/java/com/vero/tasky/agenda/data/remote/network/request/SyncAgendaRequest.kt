package com.vero.tasky.agenda.data.remote.network.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SyncAgendaRequest (
    val deletedEventsIds : List<String>,
    val deletedTasksIds: List<String>,
    val deletedReminderIds: List<String>,
)