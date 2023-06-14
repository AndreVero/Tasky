package com.vero.tasky.agenda.presentation.agenda

import com.vero.tasky.agenda.domain.model.AgendaItem
import java.time.LocalDate

sealed interface AgendaEvent {
    object LogOut : AgendaEvent
    class OnDayClick(val date: LocalDate) : AgendaEvent
    class DeleteAgendaItem(val agendaItem: AgendaItem) : AgendaEvent
    class ChangeAgendaItemStatus(val agendaItem: AgendaItem) : AgendaEvent
}