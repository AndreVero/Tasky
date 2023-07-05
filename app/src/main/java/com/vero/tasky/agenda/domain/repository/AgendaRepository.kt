package com.vero.tasky.agenda.domain.repository

import com.vero.tasky.agenda.domain.model.AgendaItem
import kotlinx.coroutines.flow.Flow

interface AgendaRepository {

    suspend fun getAgendaForDay(from: Long, to: Long) : Flow<List<AgendaItem>>

    suspend fun updateAgendaForDay(timestamp: Long): Result<Unit>

    suspend fun syncAgenda(
        deletedEventIds : List<String>,
        deletedTaskIds: List<String>,
        deletedReminderIds: List<String>,
    ) : Result<Unit>

    suspend fun updateAgenda() : Result<Unit>
}