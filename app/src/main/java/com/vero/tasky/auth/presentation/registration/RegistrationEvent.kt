package com.vero.tasky.auth.presentation.registration

sealed interface RegistrationEvent {
    class OnEmailUpdated(val email: String) : RegistrationEvent
    class OnPasswordUpdated(val password: String) : RegistrationEvent
    class OnNameUpdated(val name: String) : RegistrationEvent
    object OnBackPressed: RegistrationEvent
    object SignUp: RegistrationEvent
}