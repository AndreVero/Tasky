package com.vero.tasky.agenda.domain.usecase

import com.vero.tasky.agenda.domain.workmanagerrunner.SyncAgendaWorkerRunner
import javax.inject.Inject

class SyncAgendaUseCase @Inject constructor(
    private val syncAgendaWorkerRunner: SyncAgendaWorkerRunner
) {
    operator fun invoke() {
        syncAgendaWorkerRunner.run()
    }
}