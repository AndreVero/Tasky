package com.vero.tasky.agenda.domain.util

import com.vero.tasky.agenda.domain.model.Day
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

object LocalDateParser {

    fun getNextDaysFromDate(date: LocalDate) : List<Day> {
        val dates = mutableListOf<Day>()
        for (i in 3 downTo 0)
            dates.add(Day(date.minusDays(i.toLong()), false))
        dates.add(Day(date, true))
        for (i in 0 until 3)
            dates.add(Day(date.plusDays(i.toLong()), false))
        return dates
    }

    fun getDayLabel(date: LocalDate): String {
        val parsedMonth = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return String.format("%02d $parsedMonth ${date.year}", date.monthValue)
    }
}