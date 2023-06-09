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
class SyncAgendaWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val agendaRepository: AgendaRepository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val deletedEventIds = inputData.getStringArray(DELETED_EVENT_IDS) ?: emptyArray()
        val deletedTaskIds = inputData.getStringArray(DELETED_TASK_IDS) ?: emptyArray()
        val deletedReminderIds = inputData.getStringArray(DELETED_REMINDER_IDS) ?: emptyArray()
        val result = agendaRepository.syncAgenda(
            deletedEventIds = deletedEventIds,
            deletedTaskIds = deletedTaskIds,
            deletedReminderIds = deletedReminderIds,
        )
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

    companion object {
        const val DELETED_EVENT_IDS = "DELETED_EVENT_IDS"
        const val DELETED_TASK_IDS = "DELETED_TASK_IDS"
        const val DELETED_REMINDER_IDS = "DELETED_REMINDER_IDS"
    }
}