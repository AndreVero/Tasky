package com.vero.tasky.auth.presentation.login

sealed interface UiLoginEvent {
    object OnSignUp : UiLoginEvent
    object OnLogIn : UiLoginEvent
    class ShowErrorMessage(val message: Int) : UiLoginEvent
}