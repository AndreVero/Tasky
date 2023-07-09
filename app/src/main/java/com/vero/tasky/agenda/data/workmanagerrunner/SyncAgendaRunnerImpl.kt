package com.vero.tasky.agenda.data.workmanagerrunner

import androidx.work.*
import com.vero.tasky.agenda.data.worker.SyncAgendaDeletedItemsWorker
import com.vero.tasky.agenda.data.worker.SyncAgendaSavedItemsWorker
import com.vero.tasky.agenda.domain.workmanagerrunner.SyncAgendaRunner

class SyncAgendaRunnerImpl(
    private val workManager: WorkManager
) : SyncAgendaRunner {

    override fun run() {
        val workRequestDeletedItems = OneTimeWorkRequestBuilder<SyncAgendaDeletedItemsWorker>()
            .setConstraints(createConstraints())
            .build()

        val workRequestUpdatedItems = OneTimeWorkRequestBuilder<SyncAgendaSavedItemsWorker>()
            .setConstraints(createConstraints())
            .build()

        workManager.beginUniqueWork(
            "sync_agenda_deleted",
            ExistingWorkPolicy.KEEP,
            workRequestDeletedItems
        ).enqueue()

        workManager.beginUniqueWork(
            "sync_agenda_saved",
            ExistingWorkPolicy.KEEP,
            workRequestUpdatedItems
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