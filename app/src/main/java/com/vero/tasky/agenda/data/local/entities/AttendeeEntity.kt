package com.vero.tasky.agenda.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("eventId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AttendeeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val userId: String,
    val email: String,
    val fullName: String,
    @ColumnInfo(index = true)
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long,
)