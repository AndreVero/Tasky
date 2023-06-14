package com.vero.tasky.agenda.domain.usecase

data class AgendaUseCases constructor(
    val getAgendaForDayUseCase: GetAgendaForDayUseCase,
    val updateAgendaForDayUseCase: UpdateAgendaForDayUseCase
)