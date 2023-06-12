package com.vero.tasky.agenda.domain.util

import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

object LocalDateParser {

    fun getNextDaysFromDate(date: LocalDateTime) : List<LocalDateTime> {
        val dates = mutableListOf<LocalDateTime>()
        for (i in 0 until 6) {
            dates.add(date.plusDays(i.toLong()))
        }
        return dates
    }

    fun getDayLabel(date: LocalDateTime): String {
        val parsedMonth = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return String.format("%02d $parsedMonth ${date.year}", date.monthValue)
    }
}