package com.vero.tasky.agenda.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.HttpException

@HiltWorker
class SyncAgendaDeletedItemsWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val agendaRepository: AgendaRepository,
    private val modifiedAgendaItemDao: ModifiedAgendaItemDao
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 3)
            return Result.failure()

        val deletedItems = modifiedAgendaItemDao.loadDeletedAgendaItems()
        val tasks = deletedItems.filter { it.agendaItemType == AgendaItemType.TASK }
        val reminders = deletedItems.filter { it.agendaItemType == AgendaItemType.REMINDER }
        val events = deletedItems.filter { it.agendaItemType == AgendaItemType.EVENT }

        if (tasks.isEmpty() && reminders.isEmpty() && events.isEmpty())
            Result.success()

        val result = agendaRepository.syncAgenda(
            deletedEventIds = events.map { it.id },
            deletedTaskIds = tasks.map { it.id },
            deletedReminderIds = reminders.map { it.id }
        )
        return if (result.isSuccess) {
            supervisorScope {
                val taskJobs = tasks.map {
                    launch { modifiedAgendaItemDao.deleteAgendaItem(it) }
                }
                val eventsJob = events.map {
                    launch { modifiedAgendaItemDao.deleteAgendaItem(it) }
                }
                val reminderJobs = reminders.map {
                    launch { modifiedAgendaItemDao.deleteAgendaItem(it) }
                }
                (taskJobs + eventsJob + reminderJobs).joinAll()
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