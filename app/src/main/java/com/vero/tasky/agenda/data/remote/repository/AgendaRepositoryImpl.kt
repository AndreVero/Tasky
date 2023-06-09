package com.vero.tasky.agenda.data.remote.repository

import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ReminderDao
import com.vero.tasky.agenda.data.local.dao.TaskDao
import com.vero.tasky.agenda.data.mappers.EventMapper
import com.vero.tasky.agenda.data.mappers.ReminderMapper
import com.vero.tasky.agenda.data.mappers.TaskMapper
import com.vero.tasky.agenda.data.remote.network.AgendaApi
import com.vero.tasky.agenda.data.remote.network.dto.AgendaDto
import com.vero.tasky.agenda.data.remote.network.request.SyncAgendaRequest
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import com.vero.tasky.core.data.remote.ApiCallHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

        ApiCallHandler.safeApiCall {
            val networkAgendaItems = api.getAgendaForDay(timezone, timestamp)
            saveAgendaItems(networkAgendaItems)
        }

        emit(getAgendaItemsFromDao(timestamp))
    }

    override suspend fun syncAgenda(
        deletedEventsIds: List<String>,
        deletedTasksIds: List<String>,
        deletedReminderIds: List<String>
    ): Result<Unit> = ApiCallHandler.safeApiCall {
        api.syncAgenda(
            SyncAgendaRequest(
                deletedEventsIds = deletedEventsIds,
                deletedTasksIds = deletedTasksIds,
                deletedReminderIds = deletedReminderIds
            )
        )
    }

    override suspend fun getFullAgenda() = ApiCallHandler.safeApiCall {
        val result = api.getFullAgenda()
        saveAgendaItems(result)
    }

    private fun getAgendaItemsFromDao(timestamp: Long) : List<AgendaItem> {
        val agendaItems = mutableListOf<AgendaItem>()
        agendaItems.addAll(taskDao.loadTasksForDay(timestamp).map {
            TaskMapper.toTask(it)
        })
        agendaItems.addAll(eventDao.loadEventsForDay(timestamp).map {
            EventMapper.toEvent(it)
        })
        agendaItems.addAll(reminderDao.loadRemindersForDay(timestamp).map {
            ReminderMapper.toReminder(it)
        })
        return agendaItems
    }

    private suspend fun saveAgendaItems(agendaDto: AgendaDto) {
        supervisorScope {
            agendaDto.events.map { eventDto ->
                launch { eventDao.insertEvents(EventMapper.toEventEntity(eventDto)) }
            }.forEach { job -> job.join() }
            agendaDto.tasks.map { taskDto ->
                launch { taskDao.insertTasks(TaskMapper.toTaskEntity(taskDto)) }
            }.forEach { job -> job.join() }
            agendaDto.reminders.map { reminderDto ->
                launch { reminderDao.insertReminders(ReminderMapper.toReminderEntity(reminderDto)) }
            }.forEach { job -> job.join() }
        }
    }
}