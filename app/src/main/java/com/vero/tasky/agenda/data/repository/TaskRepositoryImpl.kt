package com.vero.tasky.agenda.data.repository

import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.local.dao.TaskDao
import com.vero.tasky.agenda.data.local.entities.ModifiedAgendaItemEntity
import com.vero.tasky.agenda.data.mappers.toEventEntity
import com.vero.tasky.agenda.data.mappers.toTask
import com.vero.tasky.agenda.data.mappers.toTaskEntity
import com.vero.tasky.agenda.data.remote.network.api.TaskApi
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.remindermanager.AlarmData
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.agenda.domain.repository.TaskRepository
import com.vero.tasky.agenda.domain.usecase.SyncAgendaUseCase
import com.vero.tasky.core.data.remote.safeSuspendCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class TaskRepositoryImpl(
    private val api: TaskApi,
    private val taskDao: TaskDao,
    private val saveTaskRunner: SaveTaskRunner,
    private val modifiedAgendaItemDao: ModifiedAgendaItemDao,
    private val alarmHandler: AlarmHandler,
    private val syncAgendaUseCase: SyncAgendaUseCase,
) : TaskRepository {

    override suspend fun saveTask(
        task: AgendaItem.Task,
        modificationType: ModificationType
    ): Result<Unit> {
        alarmHandler.setAlarm(
            AlarmData(
                time = task.remindAt,
                itemId = task.id,
                title = task.title,
                description = task.description,
                type = AgendaItemType.TASK.toString()
            )
        )
        taskDao.insertTasks(task.toTaskEntity())
        saveTaskRunner.run(
            modificationType = modificationType,
            taskId = task.id
        )
        return Result.success(Unit)
    }

    override fun getTask(id: String): Flow<AgendaItem.Task> {
        return taskDao.loadTaskFlow(id).mapNotNull { it?.toTask() }
    }

    override suspend fun fetchTask(id: String) {
        safeSuspendCall {
            val result = api.fetchTask(id)
            taskDao.insertTasks(result.toTaskEntity())
        }
    }

    override suspend fun deleteTask(task: AgendaItem.Task) {
        alarmHandler.cancelAlarm(task.id)
        taskDao.deleteTasks(task.toTaskEntity())

        safeSuspendCall {
            api.deleteTask(taskId = task.id)
        }.onFailure {
            modifiedAgendaItemDao.insertAgendaItem(
                ModifiedAgendaItemEntity(
                    id = task.id,
                    agendaItemType = AgendaItemType.TASK,
                    modificationType = ModificationType.DELETED
                )
            )
            syncAgendaUseCase()
        }
    }

}