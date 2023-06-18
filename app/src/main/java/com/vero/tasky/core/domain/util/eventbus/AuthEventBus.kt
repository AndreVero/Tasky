package com.vero.tasky.core.domain.util.eventbus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AuthEventBus {

    private val _authFlow = MutableSharedFlow<AuthEventBusEvent>()
    val authFlow = _authFlow.asSharedFlow()

    suspend fun sendEvent(event: AuthEventBusEvent) {
        _authFlow.emit(event)
    }
}