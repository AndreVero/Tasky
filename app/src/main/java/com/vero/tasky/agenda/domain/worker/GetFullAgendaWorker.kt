package com.vero.tasky.agenda.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException

@HiltWorker
class GetFullAgendaWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val agendaRepository: AgendaRepository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val result = agendaRepository.getFullAgenda()
        if (result.isSuccess)
            return Result.success()
        else {
            result.exceptionOrNull()?.let { exception ->
                if (exception is HttpException && exception.code() == 500)
                    return Result.retry()
            }
        }
        return Result.failure()
    }
}