package com.vero.tasky.auth.presentation.login

sealed interface LoginEvent {
    class OnEmailUpdated(val email: String) : LoginEvent
    class OnPasswordUpdated(val password: String) : LoginEvent
    object LogIn: LoginEvent
    object SignUp: LoginEvent
}