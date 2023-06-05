package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vero.tasky.agenda.domain.model.Attendee
import com.vero.tasky.agenda.domain.model.NetworkPhoto

@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val remindAt: Long,
    val time: Long,
    val to: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: List<Attendee>,
    val networkPhotos: List<NetworkPhoto>
)