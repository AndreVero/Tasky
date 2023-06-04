package com.vero.tasky.agenda.domain.repository

import com.vero.tasky.agenda.domain.model.Agenda
import kotlinx.coroutines.flow.Flow

interface AgendaRepository {

    suspend fun getAgendaForDay(
        timezone: String,
        time: Long
    ) : Flow<Result<Agenda>>

    suspend fun syncAgenda(
        deletedEventsIds : List<String>?,
        deletedTasksIds: List<String>?,
        deletedReminderIds: List<String>?,
    ) : Result<Unit>

    suspend fun getFullAgenda() : Result<Agenda>
}