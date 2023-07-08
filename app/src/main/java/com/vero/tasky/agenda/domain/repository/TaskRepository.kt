package com.vero.tasky.agenda.domain.repository

import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.ModificationType
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun saveTask(
        task: AgendaItem.Task,
        modificationType: ModificationType
    ) : Result<Unit>

    fun getTask(id: String): Flow<AgendaItem.Task>

    suspend fun fetchTask(id: String)

    suspend fun deleteTask(task: AgendaItem.Task)
}