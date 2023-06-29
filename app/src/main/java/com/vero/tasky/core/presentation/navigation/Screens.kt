package com.vero.tasky.core.presentation.navigation

sealed class Screens (val route: String) {
    object Agenda : Screens("agenda")
    object Event : Screens("event")
    object Task : Screens("task")
    object Reminder : Screens("reminder")
    object Login : Screens("login")
    object Registration : Screens("registration")
    object EditTitle : Screens("edit_title")
    object EditDescription : Screens("edit_description")
}
