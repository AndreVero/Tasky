package com.vero.tasky.core.domain.util.eventbus

sealed interface AuthEventBusEvent {
    object UnauthorizedException: AuthEventBusEvent
    object LogOut: AuthEventBusEvent
    object LogIn: AuthEventBusEvent
}