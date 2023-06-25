package com.vero.tasky.agenda.domain.remindermanager

import java.time.LocalDateTime

data class ReminderData(
    val time: LocalDateTime,
    val itemId: String,
    val title: String,
    val description: String? = null
)