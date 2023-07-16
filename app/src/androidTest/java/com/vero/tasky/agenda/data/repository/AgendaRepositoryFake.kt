package com.vero.tasky.agenda.data.repository

import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AgendaRepositoryFake : AgendaRepository {

    var result = Result.success(Unit)
    var agendaItems = emptyList<AgendaItem>()

    override suspend fun getAgendaForDay(from: Long, to: Long): Flow<List<AgendaItem>> {
        return flowOf(agendaItems)
    }

    override suspend fun updateAgendaForDay(timestamp: Long): Result<Unit> {
        return result
    }

    override suspend fun syncAgenda(
        deletedEventIds: List<String>,
        deletedTaskIds: List<String>,
        deletedReminderIds: List<String>
    ): Result<Unit> {
        return result
    }

    override suspend fun updateAgenda(): Result<Unit> {
        return result
    }
}