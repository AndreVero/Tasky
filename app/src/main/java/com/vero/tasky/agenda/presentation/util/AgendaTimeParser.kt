package com.vero.tasky.agenda.presentation.util

import com.vero.tasky.agenda.domain.model.AgendaItem
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

object AgendaTimeParser {

    fun getTimeLine(agendaItem: AgendaItem) : String {
        return when (agendaItem) {
            is AgendaItem.Event -> {
                val from = getParsedTimeLine(dateTime = agendaItem.time)
                val to = getParsedTimeLine(dateTime = agendaItem.to)
                "$from - $to"
            }
            is AgendaItem.Task -> getParsedTimeLine(dateTime = agendaItem.time)
            is AgendaItem.Reminder -> getParsedTimeLine(dateTime = agendaItem.time)
        }
    }

    private fun getParsedTimeLine(dateTime: LocalDateTime) : String {
        val day = dateTime.dayOfMonth
        val month = dateTime.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        return "$month $day, $hour:$minute"
    }
}