package com.vero.tasky.agenda.data.workmanagerrunner

import androidx.work.*
import com.vero.tasky.agenda.data.worker.UpdateAgendaWorker
import com.vero.tasky.agenda.domain.workmanagerrunner.UpdateAgendaRunner
import java.util.concurrent.TimeUnit

class UpdateAgendaRunnerImpl(
    private val workManager: WorkManager
) : UpdateAgendaRunner {

    override fun run()  {
        val workRequest = PeriodicWorkRequestBuilder<UpdateAgendaWorker>(
            repeatInterval = 30L, TimeUnit.MINUTES
        )
            .setConstraints(createConstraints())
            .build()

        workManager.enqueueUniquePeriodicWork(
            "get_full_agenda",
            ExistingPeriodicWorkPolicy.KEEP,
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