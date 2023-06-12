package com.vero.tasky.agenda.domain.usecase

import com.vero.tasky.agenda.domain.repository.AgendaRepository

class UpdateAgendaForDayUseCase(
    private val agendaRepository: AgendaRepository
) {

    suspend operator fun invoke(timezone: String, timestamp: Long) {
        agendaRepository.updateAgendaForDay(timezone, timestamp)
    }

}