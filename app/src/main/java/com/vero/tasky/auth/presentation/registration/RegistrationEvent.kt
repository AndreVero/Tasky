package com.vero.tasky.auth.presentation.registration

sealed interface RegistrationEvent {
    class OnEmailUpdated(val email: String) : RegistrationEvent
    class OnNameUpdated(val name: String) : RegistrationEvent
    class OnPasswordUpdated(val password: String) : RegistrationEvent
    object SignUp: RegistrationEvent
}