package com.vero.tasky.agenda.data.workmanagerrunner

import androidx.work.*
import com.vero.tasky.agenda.domain.worker.SyncAgendaWorker
import com.vero.tasky.agenda.domain.workmanagerrunner.SyncAgendaWorkerRunner

class SyncAgendaWorkerRunnerImpl(
    private val workManager: WorkManager
) : SyncAgendaWorkerRunner {

    override fun run() {
        val workRequest = OneTimeWorkRequestBuilder<SyncAgendaWorker>()
            .setConstraints(createConstraints())
            .build()

        workManager.beginUniqueWork(
            "sync_agenda",
            ExistingWorkPolicy.KEEP,
            workRequest
        ).enqueue()
    }

    private fun createConstraints() : Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(
                NetworkType.CONNECTED
            )
            .build()
    }
}