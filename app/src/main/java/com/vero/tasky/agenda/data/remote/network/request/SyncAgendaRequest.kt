package com.vero.tasky.agenda.data.remote.network.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SyncAgendaRequest (
    val deletedEventIds : List<String>,
    val deletedTaskIds: List<String>,
    val deletedReminderIds: List<String>,
)