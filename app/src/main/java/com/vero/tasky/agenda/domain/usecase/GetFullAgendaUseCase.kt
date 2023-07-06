package com.vero.tasky.agenda.domain.usecase

import com.vero.tasky.agenda.domain.workmanagerrunner.UpdateAgendaRunner
import javax.inject.Inject

class GetFullAgendaUseCase @Inject constructor(
    private val updateAgendaRunner: UpdateAgendaRunner
) {
    operator fun invoke() { updateAgendaRunner.run() }

}