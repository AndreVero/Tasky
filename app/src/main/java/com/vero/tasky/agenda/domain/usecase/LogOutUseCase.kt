package com.vero.tasky.agenda.domain.usecase

import com.vero.tasky.agenda.domain.repository.AgendaRepository

data class LogOutUseCase(
    private val agendaRepository: AgendaRepository
) {
    suspend operator fun invoke() : Result<Unit> {
        return agendaRepository.logOut()
    }
}