package com.vero.tasky.agenda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.model.ModificationType

@Entity(tableName = "modified_agenda_item")
data class ModifiedAgendaItemEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val agendaItemType: AgendaItemType,
    val modificationType: ModificationType
)