package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_reminder")
data class DeletedReminderEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String
)