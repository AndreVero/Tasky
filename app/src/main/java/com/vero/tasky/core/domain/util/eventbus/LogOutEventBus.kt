package com.vero.tasky.core.domain.util.eventbus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class LogOutEventBus {

    private val _logOutFlow = MutableSharedFlow<LogOutEventBusEvent>()
    val logOutFlow = _logOutFlow.asSharedFlow()

    suspend fun sendEvent(event: LogOutEventBusEvent) {
        _logOutFlow.emit(event)
    }
}