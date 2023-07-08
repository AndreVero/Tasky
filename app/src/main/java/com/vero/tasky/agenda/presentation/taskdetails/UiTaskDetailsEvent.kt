package com.vero.tasky.agenda.presentation.taskdetails

import androidx.annotation.StringRes

sealed interface UiTaskDetailsEvent {
    data class ShowErrorMessage(@StringRes val message: Int) : UiTaskDetailsEvent
    object OnBackClick : UiTaskDetailsEvent
}