package com.vero.tasky.auth.presentation.login

import androidx.annotation.StringRes

sealed interface UiLoginEvent {
    object OnSignUp : UiLoginEvent
    data class ShowErrorMessage(@StringRes val message: Int) : UiLoginEvent
}