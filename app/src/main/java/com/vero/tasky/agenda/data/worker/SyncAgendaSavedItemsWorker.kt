package com.vero.tasky.agenda.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.local.dao.ReminderDao
import com.vero.tasky.agenda.data.local.dao.TaskDao
import com.vero.tasky.agenda.data.mappers.toReminder
import com.vero.tasky.agenda.data.mappers.toTask
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.repository.ReminderRepository
import com.vero.tasky.agenda.domain.repository.TaskRepository
import com.vero.tasky.core.data.remote.safeSuspendCall
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.HttpException

@HiltWorker
class SyncAgendaSavedItemsWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository,
    private val modifiedAgendaItemDao: ModifiedAgendaItemDao,
    private val reminderDao: ReminderDao,
    private val taskDao: TaskDao,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 3)
            return Result.failure()

        val createdItems = modifiedAgendaItemDao.loadCreatedAgendaItems()
        val createdTasks = createdItems.filter { it.agendaItemType == AgendaItemType.TASK }
        val createdReminders = createdItems.filter { it.agendaItemType == AgendaItemType.REMINDER }

        val updatedItems = modifiedAgendaItemDao.loadUpdatedAgendaItems()
        val updatedTasks = updatedItems.filter { it.agendaItemType == AgendaItemType.TASK }
        val updatedReminders = updatedItems.filter { it.agendaItemType == AgendaItemType.REMINDER }

        if (createdTasks.isEmpty() && createdReminders.isEmpty()
            && updatedTasks.isEmpty() && updatedReminders.isEmpty()
        )
            return Result.success()

        val result = safeSuspendCall {
            supervisorScope {
                val createdTaskJobs = createdTasks.map { modifiedAgendaItem ->
                    launch {
                        taskRepository.saveTask(
                            task = taskDao.loadTask(modifiedAgendaItem.id).toTask(),
                            modificationType = ModificationType.CREATED
                        ).onSuccess { modifiedAgendaItemDao.deleteAgendaItem(modifiedAgendaItem) }
                    }
                }
                val updatedTaskJobs = updatedTasks.map { modifiedAgendaItem ->
                    launch {
                        taskRepository.saveTask(
                            task = taskDao.loadTask(modifiedAgendaItem.id).toTask(),
                            modificationType = ModificationType.UPDATED
                        ).onSuccess { modifiedAgendaItemDao.deleteAgendaItem(modifiedAgendaItem) }
                    }
                }
                val createdRemindersJobs = createdReminders.map { modifiedAgendaItem ->
                    launch {
                        reminderRepository.saveReminder(
                            reminder = reminderDao.loadReminder(modifiedAgendaItem.id).toReminder(),
                            modificationType = ModificationType.CREATED
                        )
                        modifiedAgendaItemDao.deleteAgendaItem(modifiedAgendaItem)
                    }
                }
                val updatedRemindersJobs = updatedReminders.map { modifiedAgendaItem ->
                    launch {
                        taskRepository.saveTask(
                            task = taskDao.loadTask(modifiedAgendaItem.id).toTask(),
                            modificationType = ModificationType.UPDATED
                        ).onSuccess { modifiedAgendaItemDao.deleteAgendaItem(modifiedAgendaItem) }
                    }
                }
                (createdRemindersJobs + updatedRemindersJobs
                        + createdTaskJobs + updatedTaskJobs)
                    .joinAll()
            }
        }

        return if (result.isSuccess) {
            Result.success()
        } else {
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