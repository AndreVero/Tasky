package com.vero.tasky.agenda.data.local

import androidx.room.TypeConverter
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.model.ModificationType

class Converters {

    @TypeConverter
    fun fromAgendaItemType(type: AgendaItemType) : String {
        return type.value
    }

    @TypeConverter
    fun toAgendaItemType(value: String) : AgendaItemType {
        return when (value) {
            AgendaItemType.TASK.value -> AgendaItemType.TASK
            AgendaItemType.EVENT.value -> AgendaItemType.EVENT
            AgendaItemType.REMINDER.value -> AgendaItemType.REMINDER
            else -> AgendaItemType.EVENT
        }
    }

    @TypeConverter
    fun fromModificationType(type: ModificationType) : String {
        return type.value
    }

    @TypeConverter
    fun toModificationType(value: String) : ModificationType {
        return when (value) {
            ModificationType.CREATED.value -> ModificationType.CREATED
            ModificationType.DELETED.value -> ModificationType.DELETED
            ModificationType.UPDATED.value -> ModificationType.UPDATED
            else -> ModificationType.CREATED
        }
    }

}