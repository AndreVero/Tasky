package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_event")
data class DeletedEventEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String
)