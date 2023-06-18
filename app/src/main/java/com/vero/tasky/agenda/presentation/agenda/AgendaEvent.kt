package com.vero.tasky.agenda.presentation.agenda

import com.vero.tasky.agenda.domain.model.AgendaItem
import java.time.LocalDate

sealed interface AgendaEvent {
    object LogOut : AgendaEvent
    object OnNewItemClick : AgendaEvent
    class OnDayClick(val date: LocalDate) : AgendaEvent
    class DeleteAgendaItem(val agendaItem: AgendaItem) : AgendaEvent
    class EditAgendaItem(val agendaItem: AgendaItem) : AgendaEvent
    class OpenAgendaItem(val agendaItem: AgendaItem) : AgendaEvent
    class OnCheckChanged(val agendaItem: AgendaItem.Task) : AgendaEvent
    class ChangeAgendaItemStatus(val agendaItem: AgendaItem) : AgendaEvent
}