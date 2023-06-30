package com.vero.tasky.agenda.presentation.util

import com.vero.tasky.agenda.presentation.agenda.model.Day
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*

object LocalDateParser {

    fun getNextDaysFromDate(date: LocalDate) : List<Day> {
        val dates = mutableListOf<Day>()
        for (i in 3 downTo 1)
            dates.add(Day(date.minusDays(i.toLong()), false))
        dates.add(Day(date, true))
        for (i in 1..3)
            dates.add(Day(date.plusDays(i.toLong()), false))
        return dates
    }

    fun getDayLabel(date: LocalDate): String {
        val parsedMonth = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return String.format("%02d $parsedMonth ${date.year}", date.dayOfMonth)
    }

    fun getTimeLabel(date: LocalDateTime): String {
        return String.format("%02d:%02d", date.hour, date.minute)
    }

    fun getShortDayLabel(date: LocalDateTime): String {
        val parsedMonth = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        return String.format("$parsedMonth %02d ${date.year}", date.dayOfMonth)
    }

    fun updateLocalDateTime(localDateTime: LocalDateTime, localTime: LocalTime) : LocalDateTime {
        return localDateTime.withHour(localTime.hour).withMinute(localTime.minute)
    }

    fun updateLocalDateTime(localDateTime: LocalDateTime, localDate: LocalDate) : LocalDateTime {
        return localDate.atTime(localDateTime.hour, localDateTime.minute)
    }
}