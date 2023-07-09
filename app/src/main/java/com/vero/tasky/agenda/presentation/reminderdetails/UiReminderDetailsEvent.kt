package com.vero.tasky.agenda.presentation.reminderdetails

sealed interface UiReminderDetailsEvent {
    object OnBackClick : UiReminderDetailsEvent
}