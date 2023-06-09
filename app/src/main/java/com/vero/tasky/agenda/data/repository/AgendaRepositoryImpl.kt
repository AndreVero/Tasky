package com.vero.tasky.agenda.data.repository

import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ReminderDao
import com.vero.tasky.agenda.data.local.dao.TaskDao
import com.vero.tasky.agenda.data.mappers.*
import com.vero.tasky.agenda.data.remote.network.AgendaApi
import com.vero.tasky.agenda.data.remote.network.dto.AgendaDto
import com.vero.tasky.agenda.data.remote.network.request.SyncAgendaRequest
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import com.vero.tasky.core.data.remote.safeApiCall
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class AgendaRepositoryImpl(
    private val api: AgendaApi,
    private val taskDao: TaskDao,
    private val eventDao: EventDao,
    private val reminderDao: ReminderDao,
) : AgendaRepository {

    override suspend fun getAgendaForDay(
        timezone: String,
        timestamp: Long
    ) : Flow<List<AgendaItem>> = flow {

        emit(getAgendaItemsFromDao(timestamp))

        safeApiCall {
            val networkAgendaItems = api.getAgendaForDay(timezone, timestamp)
            saveAgendaItems(networkAgendaItems)
        }

        emit(getAgendaItemsFromDao(timestamp))
    }

    override suspend fun syncAgenda(
        deletedEventIds: Array<String>,
        deletedTaskIds: Array<String>,
        deletedReminderIds: Array<String>
    ): Result<Unit> = safeApiCall {
        api.syncAgenda(
            SyncAgendaRequest(
                deletedEventIds = deletedEventIds,
                deletedTaskIds = deletedTaskIds,
                deletedReminderIds = deletedReminderIds
            )
        )
    }

    override suspend fun getFullAgenda() = safeApiCall {
        val result = api.getFullAgenda()
        saveAgendaItems(result)
    }

    private fun getAgendaItemsFromDao(timestamp: Long) : List<AgendaItem> {
        val agendaItems = mutableListOf<AgendaItem>()
        agendaItems.addAll(taskDao.loadTasksForDay(timestamp).map { it.toTask() })
        agendaItems.addAll(eventDao.loadEventsForDay(timestamp).map {it.toEvent() })
        agendaItems.addAll(reminderDao.loadRemindersForDay(timestamp).map { it.toReminder() })
        return agendaItems
    }

    private suspend fun saveAgendaItems(agendaDto: AgendaDto) {
        supervisorScope {
            val jobsList = mutableListOf<Job>()
            agendaDto.events.map { eventDto ->
                jobsList.add(launch { eventDao.insertEvents(eventDto.toEventEntity()) })
            }
            agendaDto.tasks.map { taskDto ->
                jobsList.add(launch { taskDao.insertTasks(taskDto.toTaskEntity()) })
            }
            agendaDto.reminders.map { reminderDto ->
                jobsList.add(launch { reminderDao.insertReminders(reminderDto.toReminderEntity()) })
            }
            jobsList.joinAll()
        }
    }
}