package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vero.tasky.agenda.domain.model.AgendaItemType

@Entity(tableName = "deleted_item")
data class DeletedAgendaItemEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val type: AgendaItemType
)