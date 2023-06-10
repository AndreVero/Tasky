package com.vero.tasky.agenda.domain.usecase

import androidx.work.*
import com.vero.tasky.agenda.domain.worker.SyncAgendaWorker
import javax.inject.Inject

class SyncAgendaUseCase @Inject constructor(private val workManager: WorkManager) {

    operator fun invoke(deletedEventsIds : Array<String>,
                                deletedTasksIds: Array<String>,
                                deletedReminderIds: Array<String>) {
        val workRequest = OneTimeWorkRequestBuilder<SyncAgendaWorker>()
            .setInputData(createInputData(deletedEventsIds, deletedTasksIds, deletedReminderIds))
            .setConstraints(createConstraints())
            .build()

        workManager.beginUniqueWork(
            "sync_agenda",
            ExistingWorkPolicy.KEEP,
            workRequest
        ).enqueue()
    }

    private fun createInputData(deletedEventsIds : Array<String>,
                                deletedTasksIds: Array<String>,
                                deletedReminderIds: Array<String>): Data {
        val builder = Data.Builder()
        builder.putStringArray(SyncAgendaWorker.DELETED_EVENT_IDS, deletedEventsIds)
        builder.putStringArray(SyncAgendaWorker.DELETED_TASK_IDS, deletedTasksIds)
        builder.putStringArray(SyncAgendaWorker.DELETED_REMINDER_IDS, deletedReminderIds)
        return builder.build()
    }

    private fun createConstraints() : Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(
                NetworkType.CONNECTED
            )
            .build()
    }
}