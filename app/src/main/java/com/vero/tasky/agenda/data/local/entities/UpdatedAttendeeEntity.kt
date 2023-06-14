package com.vero.tasky.agenda.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = UpdatedEventEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("eventId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UpdatedAttendeeEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    @ColumnInfo(index = true)
    val eventId: String,
)