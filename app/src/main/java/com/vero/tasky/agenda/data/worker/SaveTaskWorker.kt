package com.vero.tasky.agenda.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.local.dao.TaskDao
import com.vero.tasky.agenda.data.local.entities.ModifiedAgendaItemEntity
import com.vero.tasky.agenda.data.mappers.*
import com.vero.tasky.agenda.data.remote.network.api.TaskApi
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveTaskRunner.Companion.MODIFICATION_TYPE
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveTaskRunner.Companion.TASK_ID
import com.vero.tasky.core.data.remote.safeSuspendCall
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SaveTaskWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val modifiedAgendaItemDao: ModifiedAgendaItemDao,
    private val taskDao: TaskDao,
    private val api: TaskApi,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val taskId = inputData.getString(TASK_ID)
        val modificationTypeStr = inputData.getString(MODIFICATION_TYPE)

        if (taskId == null || modificationTypeStr == null)
            return Result.success()

        val modificationType = ModificationType.valueOf(modificationTypeStr)

        if (runAttemptCount >= 3) {
            modifiedAgendaItemDao.insertAgendaItem(
                ModifiedAgendaItemEntity(
                    id = taskId,
                    modificationType = modificationType,
                    agendaItemType = AgendaItemType.TASK
                )
            )
            return Result.failure()
        }

        val result = safeSuspendCall {
            val taskEntity = taskDao.loadTask(taskId)


            when(modificationType) {
                ModificationType.CREATED -> api.createTask(taskEntity.toTaskRequest())
                ModificationType.UPDATED -> api.updateTask(taskEntity.toTaskRequest())
                else -> return@safeSuspendCall
            }
        }
        return if (result.isSuccess)
            Result.success()
        else
            Result.retry()
    }
}