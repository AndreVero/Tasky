package com.vero.tasky.agenda.presentation.agenda

import androidx.annotation.StringRes

sealed interface UiAgendaEvent {
    object OnLogOut : UiAgendaEvent
    data class ShowErrorMessage(@StringRes val message: Int) : UiAgendaEvent
}