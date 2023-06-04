package com.vero.tasky.agenda.data.remote.repository

import com.vero.tasky.agenda.data.remote.network.AgendaApi
import com.vero.tasky.agenda.data.remote.network.request.SyncAgendaRequest
import com.vero.tasky.agenda.domain.model.Agenda
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import com.vero.tasky.core.data.remote.ApiCallHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AgendaRepositoryImpl(
    private val api: AgendaApi
) : AgendaRepository {

    override suspend fun getAgendaForDay(timezone: String, time: Long): Flow<Result<Agenda>> = flow {
    }

    override suspend fun syncAgenda(
        deletedEventsIds: List<String>?,
        deletedTasksIds: List<String>?,
        deletedReminderIds: List<String>?
    ): Result<Unit> = ApiCallHandler.safeApiCall {
            api.syncAgenda(
                SyncAgendaRequest(
                    deletedEventsIds = deletedEventsIds,
                    deletedTasksIds = deletedTasksIds,
                    deletedReminderIds = deletedReminderIds
                )
            )
    }

    override suspend fun getFullAgenda() =
        ApiCallHandler.safeApiCall {
            Agenda(emptyList(), emptyList(), emptyList())
        }
}