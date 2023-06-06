package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.model.AgendaItem

@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val title: String,
    val description: String?,
    val remindAt: Long,
    val time: Long,
)

fun ReminderEntity.toReminder() : AgendaItem.Reminder {
    return AgendaItem.Reminder(
        id = id,
        title = title,
        description = description,
        remindAt = LocalDateTimeConverter.longToLocalDateTime(remindAt),
        time = LocalDateTimeConverter.longToLocalDateTime(time)
    )
}