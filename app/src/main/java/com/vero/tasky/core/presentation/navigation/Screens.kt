package com.vero.tasky.core.presentation.navigation

sealed class Screens (val route: String) {
    object Agenda : Screens("agenda")
    object Login : Screens("login")
    object SignUp : Screens("sign_up")
}
