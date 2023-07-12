package com.vero.tasky.agenda.domain.connectivitymanager

import kotlinx.coroutines.flow.Flow

interface ConnectionHandler {

    fun isConnected() : Boolean

    fun observeConnectionState(): Flow<ConnectionStatus>

}