package com.vero.tasky.agenda.domain.repository

import com.vero.tasky.agenda.domain.model.AgendaItem
import kotlinx.coroutines.flow.Flow

interface AgendaRepository {

    suspend fun getAgendaForDay(
        timezone: String,
        timestamp: Long
    ) : Flow<List<AgendaItem>>

    suspend fun syncAgenda(
        deletedEventsIds : List<String>,
        deletedTasksIds: List<String>,
        deletedReminderIds: List<String>,
    ) : Result<Unit>

    suspend fun getFullAgenda() : Result<Unit>
}