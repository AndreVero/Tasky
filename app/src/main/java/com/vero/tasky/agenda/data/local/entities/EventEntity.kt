package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String?,
    val remindAt: Long,
    val time: Long,
    val to: Long,
    val host: String,
    val isUserEventCreator: Boolean
)