package com.vero.tasky.agenda.domain.usecase

import androidx.work.*
import com.vero.tasky.agenda.domain.worker.GetFullAgendaWorker
import javax.inject.Inject

class GetFullAgendaUseCase @Inject constructor(
    private val workManager: WorkManager
) {

    operator fun invoke()  {
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