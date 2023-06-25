package com.vero.tasky.agenda.presentation.agendaevent.model

enum class ReminderRange(val minutes: Long) {
    MINUTES_10(10),
    MINUTES_30(30),
    HOURS_1(60),
    HOURS_6(60 * 6),
    DAYS_1(60 * 24)
}