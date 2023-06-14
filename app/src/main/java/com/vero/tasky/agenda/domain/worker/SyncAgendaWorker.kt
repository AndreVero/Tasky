package com.vero.tasky.agenda.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vero.tasky.agenda.data.local.dao.DeletedEventsDao
import com.vero.tasky.agenda.data.local.dao.DeletedRemindersDao
import com.vero.tasky.agenda.data.local.dao.DeletedTasksDao
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.HttpException

@HiltWorker
class SyncAgendaWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val agendaRepository: AgendaRepository,
    private val deletedTasksDao: DeletedTasksDao,
    private val deletedRemindersDao: DeletedRemindersDao,
    private val deletedEventsDao: DeletedEventsDao
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 3)
            return Result.failure()

        val tasks = deletedTasksDao.loadTasks()
        val reminders = deletedRemindersDao.loadReminders()
        val events = deletedEventsDao.loadEvents()

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
                    launch { deletedTasksDao.deleteTask(it) }
                }
                val eventJobs = events.map {
                    launch { deletedEventsDao.deleteEvent(it) }
                }
                val reminderJobs = reminders.map {
                    launch { deletedRemindersDao.deleteReminder(it) }
                }
                (taskJobs + eventJobs + reminderJobs).joinAll()
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