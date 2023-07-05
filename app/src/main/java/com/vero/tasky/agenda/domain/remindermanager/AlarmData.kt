package com.vero.tasky.agenda.domain.remindermanager

import java.time.LocalDateTime

data class AlarmData(
    val time: LocalDateTime,
    val itemId: String,
    val title: String,
    val type: String,
    val description: String? = null
)