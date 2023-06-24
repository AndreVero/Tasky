package com.vero.tasky.agenda.data.workmanagerrunner

import androidx.work.*
import com.vero.tasky.agenda.data.worker.UpdateEventWorker
import com.vero.tasky.agenda.domain.workmanagerrunner.UpdateEventWorkerRunner
import com.vero.tasky.agenda.domain.workmanagerrunner.UpdateEventWorkerRunner.Companion.EVENT_ID
import com.vero.tasky.agenda.domain.workmanagerrunner.UpdateEventWorkerRunner.Companion.IS_GOING

class UpdateEventWorkerRunnerImpl(
    private val workManager: WorkManager
) : UpdateEventWorkerRunner {

    override fun run(isGoing: Boolean, eventId: String)  {
        val workRequest = OneTimeWorkRequestBuilder<UpdateEventWorker>()
            .setConstraints(createConstraints())
            .setInputData(Data.Builder()
                .putBoolean(IS_GOING, isGoing)
                .putString(EVENT_ID, eventId)
                .build()
            )
            .build()

        workManager.beginUniqueWork(
            "update_event_worker",
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