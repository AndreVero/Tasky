package com.vero.tasky.agenda.domain.usecase

import com.vero.tasky.agenda.domain.repository.AgendaRepository
import javax.inject.Inject

class GetFullAgendaUseCase @Inject constructor(
    private val agendaRepository: AgendaRepository
) {
    suspend operator fun invoke() { agendaRepository.updateAgenda() }

}