package com.vero.tasky.agenda.domain.usecase

import com.vero.tasky.agenda.domain.workmanagerrunner.GetFullAgendaWorkerRunner
import javax.inject.Inject

class GetFullAgendaUseCase @Inject constructor(
    private val getFullAgendaWorkerRunner: GetFullAgendaWorkerRunner
) {
    operator fun invoke() { getFullAgendaWorkerRunner.run() }

}