package com.vero.tasky.agenda.data.mappers

import com.vero.tasky.agenda.data.local.entities.DeletedAgendaItemEntity
import com.vero.tasky.agenda.domain.model.DeletedAgendaItem

fun DeletedAgendaItem.toEntity() : DeletedAgendaItemEntity {
    return DeletedAgendaItemEntity(
        id = this.id,
        type = this.type
    )
}