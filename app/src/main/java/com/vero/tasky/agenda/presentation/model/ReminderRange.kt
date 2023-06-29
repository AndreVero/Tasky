package com.vero.tasky.agenda.presentation.model

import androidx.annotation.StringRes
import com.vero.tasky.R

enum class ReminderRange(
    val minutes: Long,
    @StringRes val label: Int
) {
    MINUTES_10(10, R.string.minutes_before_10),
    MINUTES_30(30, R.string.minutes_before_30),
    HOURS_1(60, R.string.hour_before),
    HOURS_6(60 * 6, R.string.hours_before_6),
    DAYS_1(60 * 24, R.string.day_before)
}