package com.vero.tasky.agenda.domain.model

data class DeletedAgendaItem(
    val id: String,
    val type: AgendaItemType
)