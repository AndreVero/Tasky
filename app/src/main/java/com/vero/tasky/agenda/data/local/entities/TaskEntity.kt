package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.model.AgendaItem

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val title: String,
    val description: String?,
    val remindAt: Long,
    val time: Long,
    val isDone: Boolean
)

fun TaskEntity.toTask() : AgendaItem.Task {
    return AgendaItem.Task(
        id = id,
        title = title,
        description = description,
        remindAt = LocalDateTimeConverter.longToLocalDateTime(remindAt),
        time = LocalDateTimeConverter.longToLocalDateTime(time),
        isDone = isDone
    )
}