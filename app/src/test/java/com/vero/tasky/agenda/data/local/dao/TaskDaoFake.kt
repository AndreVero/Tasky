package com.vero.tasky.agenda.data.local.dao

import com.vero.tasky.agenda.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskDaoFake : TaskDao {

    private val taskList = mutableSetOf<TaskEntity>()

    override suspend fun insertTasks(vararg tasks: TaskEntity) {
        taskList.addAll(tasks)
    }

    override suspend fun updateTasks(vararg tasks: TaskEntity) {}

    override suspend fun deleteTasks(vararg tasks: TaskEntity) {}

    override fun loadTasksForDay(from: Long, to: Long): Flow<List<TaskEntity>> {

        return flow {
            emit(taskList.toList())
        }
    }

    override suspend fun loadAllTasks(): List<TaskEntity> {
        return taskList.toList()
    }
}