package com.vero.tasky.agenda.domain.repository

import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.ModificationType

interface TaskRepository {

    suspend fun saveTask(
        task: AgendaItem.Task,
        modificationType: ModificationType
    )

    suspend fun getTask(id: String): AgendaItem.Task

    suspend fun deleteTask(task: AgendaItem.Task)
}