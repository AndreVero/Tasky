package com.vero.tasky.agenda.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException

@HiltWorker
class UpdateAgendaWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val agendaRepository: AgendaRepository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 3)
            return Result.failure()
        val result = agendaRepository.updateAgenda()
        return if (result.isSuccess)
            Result.success()
        else {
            result.exceptionOrNull()?.let { exception ->
                if (exception is HttpException && exception.code() == 401)
                    Result.failure()
                else
                    Result.retry()
            }
            Result.retry()
        }
    }
}