package com.vero.tasky.agenda.presentation.agendaevent

import androidx.annotation.StringRes

sealed interface UiEventDetailsEvent {
    data class ShowErrorMessage(@StringRes val message: Int) : UiEventDetailsEvent
    data class ShowInfoToast(val text: String) : UiEventDetailsEvent
    object OnBackClick : UiEventDetailsEvent
}