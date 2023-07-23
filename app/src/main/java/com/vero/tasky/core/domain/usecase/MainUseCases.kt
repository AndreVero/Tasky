package com.vero.tasky.core.domain.usecase

import com.vero.tasky.agenda.domain.usecase.GetFullAgendaWithWorkerUseCase
import com.vero.tasky.agenda.domain.usecase.SyncAgendaUseCase
import com.vero.tasky.auth.domain.usecase.AuthenticateUseCase
import com.vero.tasky.auth.domain.usecase.ClearDatabaseUseCase
import com.vero.tasky.auth.domain.usecase.LogOutUseCase
import javax.inject.Inject

data class MainUseCases @Inject constructor(
    val authenticateUseCase: AuthenticateUseCase,
    val syncAgendaUseCase: SyncAgendaUseCase,
    val getFullAgendaWithWorkerUseCase: GetFullAgendaWithWorkerUseCase,
    val clearDatabase: ClearDatabaseUseCase,
    val logOut: LogOutUseCase
)
