package com.vero.tasky.auth.presentation.registration

import androidx.annotation.StringRes

sealed interface UiRegistrationEvent {
    object OnSignUp : UiRegistrationEvent
    data class ShowErrorMessage(@StringRes val message: Int) : UiRegistrationEvent
}