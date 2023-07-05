package com.vero.tasky.agenda.domain.usecase

import com.vero.tasky.agenda.domain.workmanagerrunner.SyncAgendaRunner
import javax.inject.Inject

class SyncAgendaUseCase @Inject constructor(
    private val syncAgendaRunner: SyncAgendaRunner
) {
    operator fun invoke() {
        syncAgendaRunner.run()
    }
}