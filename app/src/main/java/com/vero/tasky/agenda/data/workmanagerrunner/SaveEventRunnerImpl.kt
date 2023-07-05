package com.vero.tasky.agenda.data.workmanagerrunner

import androidx.work.*
import com.vero.tasky.agenda.data.worker.SaveEventWorker
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveEventRunner
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveEventRunner.Companion.EVENT_ID
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveEventRunner.Companion.IS_GOING
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveEventRunner.Companion.MODIFICATION_TYPE

class SaveEventRunnerImpl(
    private val workManager: WorkManager
) : SaveEventRunner {

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