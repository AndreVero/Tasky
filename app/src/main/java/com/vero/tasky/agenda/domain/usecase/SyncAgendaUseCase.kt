package com.vero.tasky.agenda.domain.usecase

import com.vero.tasky.agenda.domain.workmanagerrunner.SyncAgendaWorkManagerRunner
import javax.inject.Inject

class SyncAgendaUseCase @Inject constructor(
    private val syncAgendaWorkManagerRunner: SyncAgendaWorkManagerRunner
) {
    operator fun invoke() {
        syncAgendaWorkManagerRunner.run()
    }
}