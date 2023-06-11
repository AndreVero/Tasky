package com.vero.tasky.agenda.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vero.tasky.agenda.data.local.dao.DeletedAgendaItemDao
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.HttpException

@HiltWorker
class SyncAgendaWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val agendaRepository: AgendaRepository,
    private val dao: DeletedAgendaItemDao,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 3)
            return Result.failure()

        val agendaItems = dao.loadDeletedAgendaItems()
        if (agendaItems.isEmpty())
            Result.success()

        val result = agendaRepository.syncAgenda(
            deletedEventIds = agendaItems.filter { it.type == AgendaItemType.Event }.map { it.id },
            deletedTaskIds = agendaItems.filter { it.type == AgendaItemType.Task }.map { it.id },
            deletedReminderIds = agendaItems.filter { it.type == AgendaItemType.Reminder }.map { it.id },
        )
        return if (result.isSuccess) {
            supervisorScope {
                agendaItems.map {
                    launch { dao.deleteAgendaItem(it) }
                }.forEach { it.join() }
            }
            Result.success()
        }
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