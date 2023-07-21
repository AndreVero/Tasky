package com.vero.tasky.agenda.domain.util

import com.vero.tasky.agenda.presentation.model.ReminderRange
import java.time.Duration
import java.time.LocalDateTime

object ReminderRangeParser {

    fun getRangeFromDateTime(
        reminder: LocalDateTime,
        time: LocalDateTime
    ) : ReminderRange {
        return when(Duration.between(
            time, reminder
        ).toMinutes()) {
            ReminderRange.MINUTES_10.minutes -> ReminderRange.MINUTES_10
            ReminderRange.MINUTES_30.minutes -> ReminderRange.MINUTES_30
            ReminderRange.HOURS_1.minutes -> ReminderRange.HOURS_1
            ReminderRange.HOURS_6.minutes -> ReminderRange.HOURS_6
            ReminderRange.DAYS_1.minutes -> ReminderRange.DAYS_1
            else -> ReminderRange.MINUTES_10
        }
    }
}