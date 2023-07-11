package com.vero.tasky.agenda.presentation.taskdetails

sealed interface UiTaskDetailsEvent {
    object OnBackClick : UiTaskDetailsEvent
}