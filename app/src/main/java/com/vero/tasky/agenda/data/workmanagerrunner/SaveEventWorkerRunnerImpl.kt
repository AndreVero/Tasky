package com.vero.tasky.agenda.data.workmanagerrunner

import androidx.work.*
import com.vero.tasky.agenda.data.worker.SaveEventWorker
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveEventWorkerRunner
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveEventWorkerRunner.Companion.EVENT_ID
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveEventWorkerRunner.Companion.IS_GOING
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveEventWorkerRunner.Companion.MODIFICATION_TYPE

class SaveEventWorkerRunnerImpl(
    private val workManager: WorkManager
) : SaveEventWorkerRunner {

    override fun run(isGoing: Boolean, eventId: String, modificationType: ModificationType)  {
        val workRequest = OneTimeWorkRequestBuilder<SaveEventWorker>()
            .setConstraints(createConstraints())
            .setInputData(Data.Builder()
                .putBoolean(IS_GOING, isGoing)
                .putString(EVENT_ID, eventId)
                .putString(MODIFICATION_TYPE, modificationType.toString())
                .build()
            )
            .build()

        workManager.beginUniqueWork(
            "save_event_worker:$eventId",
            ExistingWorkPolicy.APPEND,
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