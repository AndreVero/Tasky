package com.vero.tasky.agenda.data.local

import androidx.room.TypeConverter
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.model.ModificationType

class Converters {

    @TypeConverter
    fun fromAgendaItemType(type: AgendaItemType) : String {
        return type.toString()
    }

    @TypeConverter
    fun toAgendaItemType(value: String) : AgendaItemType {
        return AgendaItemType.valueOf(value)
    }

    @TypeConverter
    fun fromModificationType(type: ModificationType) : String {
        return type.toString()
    }

    @TypeConverter
    fun toModificationType(value: String) : ModificationType {
        return ModificationType.valueOf(value)
    }
}