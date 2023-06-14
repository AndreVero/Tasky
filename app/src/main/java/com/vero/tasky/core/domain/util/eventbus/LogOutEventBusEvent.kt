package com.vero.tasky.core.domain.util.eventbus

sealed interface LogOutEventBusEvent {
    object UnauthorizedException: LogOutEventBusEvent
    object LogOut: LogOutEventBusEvent
}