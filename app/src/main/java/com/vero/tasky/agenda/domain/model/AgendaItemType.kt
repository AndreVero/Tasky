package com.vero.tasky.agenda.domain.model

sealed interface AgendaItemType {
    object Task: AgendaItemType
    object Event: AgendaItemType
    object Reminder: AgendaItemType
}