package com.vero.tasky.agenda.data.workmanagerrunner

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vero.tasky.agenda.data.worker.CreateEventWorker
import com.vero.tasky.agenda.domain.workmanagerrunner.CreateEventWorkerRunner

class CreateEventWorkerRunnerImpl(
    private val workManager: WorkManager
) : CreateEventWorkerRunner {

    override fun run()  {
        val workRequest = OneTimeWorkRequestBuilder<CreateEventWorker>()
            .setConstraints(createConstraints())
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