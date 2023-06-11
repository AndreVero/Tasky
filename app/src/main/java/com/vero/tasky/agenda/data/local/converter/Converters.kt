package com.vero.tasky.agenda.data.local.converter

import androidx.room.TypeConverter
import com.vero.tasky.agenda.domain.model.AgendaItemType

class Converters {

    @TypeConverter
    fun fromAgendaItemType(agendaItemType: AgendaItemType) : String {
        return when(agendaItemType) {
            AgendaItemType.Event -> EVENT
            AgendaItemType.Task -> TASK
            AgendaItemType.Reminder -> REMINDER
        }
    }

    @TypeConverter
    fun toAgendaItemType(value: String) : AgendaItemType {
        return when(value) {
            EVENT -> AgendaItemType.Event
            TASK -> AgendaItemType.Task
            REMINDER -> AgendaItemType.Reminder
            else -> AgendaItemType.Event
        }
    }

    companion object {
        private const val EVENT = "event"
        private const val TASK = "task"
        private const val REMINDER = "reminder"
    }
}