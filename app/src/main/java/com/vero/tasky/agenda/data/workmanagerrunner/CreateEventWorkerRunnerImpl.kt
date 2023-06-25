package com.vero.tasky.agenda.data.workmanagerrunner

import androidx.work.*
import com.vero.tasky.agenda.data.worker.CreateEventWorker
import com.vero.tasky.agenda.domain.workmanagerrunner.CreateEventWorkerRunner
import com.vero.tasky.agenda.domain.workmanagerrunner.CreateEventWorkerRunner.Companion.EVENT_ID

class CreateEventWorkerRunnerImpl(
    private val workManager: WorkManager
) : CreateEventWorkerRunner {

    override fun run(eventId: String)  {
        val workRequest = OneTimeWorkRequestBuilder<CreateEventWorker>()
            .setConstraints(createConstraints())
            .setInputData(Data.Builder().putString(EVENT_ID, eventId).build())
            .build()

        workManager.beginUniqueWork(
            "create_event_worker",
            ExistingWorkPolicy.REPLACE,
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