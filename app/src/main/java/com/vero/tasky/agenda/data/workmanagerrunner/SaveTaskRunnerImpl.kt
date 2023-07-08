package com.vero.tasky.agenda.data.workmanagerrunner

import androidx.work.*
import com.vero.tasky.agenda.data.worker.SaveTaskWorker
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveTaskRunner.Companion.MODIFICATION_TYPE
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveTaskRunner
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveTaskRunner.Companion.TASK_ID

class SaveTaskRunnerImpl(
    private val workManager: WorkManager
) : SaveTaskRunner {

    override fun run(taskId: String, modificationType: ModificationType)  {
        val workRequest = OneTimeWorkRequestBuilder<SaveTaskWorker>()
            .setConstraints(createConstraints())
            .setInputData(Data.Builder()
                .putString(TASK_ID, taskId)
                .putString(MODIFICATION_TYPE, modificationType.toString())
                .build()
            )
            .build()

        workManager.beginUniqueWork(
            "save_task_worker:$taskId",
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