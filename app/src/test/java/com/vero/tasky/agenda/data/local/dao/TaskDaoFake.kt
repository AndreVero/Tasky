package com.vero.tasky.agenda.data.local.dao

import com.vero.tasky.agenda.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TaskDaoFake : TaskDao {

    private val taskList = mutableSetOf<TaskEntity>()

    override suspend fun insertTasks(vararg tasks: TaskEntity) {
        taskList.addAll(tasks)
    }

    override suspend fun updateTasks(vararg tasks: TaskEntity) {}

    override suspend fun deleteTasks(vararg tasks: TaskEntity) {
        taskList.removeAll(tasks.toSet())
    }

    override fun loadTasksForDay(from: Long, to: Long): Flow<List<TaskEntity>> {
        return  flowOf(taskList.toList())
    }

    override suspend fun loadTask(id: String): TaskEntity {
        return taskList.first{ it.id == id }
    }

    override suspend fun loadAllTasks(): List<TaskEntity> {
        return taskList.toList()
    }
}