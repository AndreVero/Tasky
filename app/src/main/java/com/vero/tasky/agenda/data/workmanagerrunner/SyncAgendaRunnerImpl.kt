package com.vero.tasky.agenda.data.workmanagerrunner

import androidx.work.*
import com.vero.tasky.agenda.data.worker.SyncDeletedItemWorker
import com.vero.tasky.agenda.domain.workmanagerrunner.SyncAgendaRunner

class SyncAgendaRunnerImpl(
    private val workManager: WorkManager
) : SyncAgendaRunner {

    override fun run() {
        val workRequest = OneTimeWorkRequestBuilder<SyncDeletedItemWorker>()
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