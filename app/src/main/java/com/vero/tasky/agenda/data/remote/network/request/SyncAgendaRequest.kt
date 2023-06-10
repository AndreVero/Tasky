package com.vero.tasky.agenda.data.remote.network.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SyncAgendaRequest (
    val deletedEventIds : Array<String>,
    val deletedTaskIds: Array<String>,
    val deletedReminderIds: Array<String>,
)