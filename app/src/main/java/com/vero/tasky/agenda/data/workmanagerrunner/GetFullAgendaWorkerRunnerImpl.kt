package com.vero.tasky.agenda.data.workmanagerrunner

import androidx.work.*
import com.vero.tasky.agenda.data.worker.GetFullAgendaWorker
import com.vero.tasky.agenda.domain.workmanagerrunner.GetFullAgendaWorkerRunner
import java.util.concurrent.TimeUnit

class GetFullAgendaWorkerRunnerImpl(
    private val workManager: WorkManager
) : GetFullAgendaWorkerRunner {

    override fun run()  {
        val workRequest = PeriodicWorkRequestBuilder<GetFullAgendaWorker>(
            repeatInterval = 30L, TimeUnit.MINUTES
        )
            .setConstraints(createConstraints())
            .build()

        workManager.enqueueUniquePeriodicWork(
            "get_full_agenda",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            workRequest
        )
    }

    private fun createConstraints() : Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(
                NetworkType.CONNECTED
            )
            .build()
    }
}