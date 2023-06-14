package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "created_reminder")
data class CreatedReminderEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String?,
    val remindAt: Long,
    val time: Long,
)

