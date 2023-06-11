package com.vero.tasky.agenda.domain.usecase

import com.vero.tasky.agenda.domain.workmanagerrunner.GetFullAgendaWorkManagerRunner
import javax.inject.Inject

class GetFullAgendaUseCase @Inject constructor(
    private val getFullAgendaWorkManagerRunner: GetFullAgendaWorkManagerRunner
) {
    operator fun invoke() { getFullAgendaWorkManagerRunner.run() }

}