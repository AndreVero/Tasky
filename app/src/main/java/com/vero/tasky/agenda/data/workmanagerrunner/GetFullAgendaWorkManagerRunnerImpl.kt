package com.vero.tasky.agenda.data.workmanagerrunner

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vero.tasky.agenda.domain.worker.GetFullAgendaWorker
import com.vero.tasky.agenda.domain.workmanagerrunner.GetFullAgendaWorkManagerRunner
import javax.inject.Inject

class GetFullAgendaWorkManagerRunnerImpl @Inject constructor(
    private val workManager: WorkManager
) : GetFullAgendaWorkManagerRunner {

    override fun run()  {
        val workRequest = OneTimeWorkRequestBuilder<GetFullAgendaWorker>()
            .setConstraints(createConstraints())
            .build()

        workManager.beginUniqueWork(
            "get_full_agenda",
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