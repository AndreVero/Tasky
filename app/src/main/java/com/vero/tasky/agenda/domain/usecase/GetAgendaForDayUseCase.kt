package com.vero.tasky.agenda.domain.usecase

import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import kotlinx.coroutines.flow.Flow

class GetAgendaForDayUseCase(
    private val agendaRepository: AgendaRepository
    ) {

    suspend operator fun invoke(from: Long, to: Long) : Flow<List<AgendaItem>> {
        return agendaRepository.getAgendaForDay(from, to)
    }

}