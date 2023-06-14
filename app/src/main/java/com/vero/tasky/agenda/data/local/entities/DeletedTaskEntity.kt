package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_task")
data class DeletedTaskEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String
)