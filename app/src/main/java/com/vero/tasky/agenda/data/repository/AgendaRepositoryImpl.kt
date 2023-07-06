package com.vero.tasky.agenda.data.repository

import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ReminderDao
import com.vero.tasky.agenda.data.local.dao.TaskDao
import com.vero.tasky.agenda.data.mappers.*
import com.vero.tasky.agenda.data.remote.network.api.AgendaApi
import com.vero.tasky.agenda.data.remote.network.dto.AgendaDto
import com.vero.tasky.agenda.data.remote.network.request.SyncAgendaRequest
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import com.vero.tasky.core.data.remote.safeSuspendCall
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import java.util.*

class AgendaRepositoryImpl(
    private val api: AgendaApi,
    private val taskDao: TaskDao,
    private val eventDao: EventDao,
    private val reminderDao: ReminderDao,
    private val alarmHandler: AlarmHandler,
) : AgendaRepository {

    override suspend fun getAgendaForDay(from: Long, to: Long) : Flow<List<AgendaItem>> {
        val taskFlow = taskDao.loadTasksForDay(from, to).map {
            it.map { taskEntity ->  taskEntity.toTask() }
        }
        val eventFlow = eventDao.loadEventsForDay(from, to).map {
            it.map { eventEntity ->  eventEntity.toEvent() }
        }
        val reminderFlow = reminderDao.loadRemindersForDay(from, to).map {
            it.map { reminderEntity ->  reminderEntity.toReminder() }
        }
        return merge(taskFlow, eventFlow, reminderFlow).map { it.sortedBy { item -> item.time } }
    }

    override suspend fun updateAgendaForDay(timestamp: Long): Result<Unit> {
        return safeSuspendCall {
            val networkAgendaItems = api.getAgendaForDay(TimeZone.getDefault().id, timestamp)
            saveAgendaItems(networkAgendaItems)
        }
    }

    override suspend fun syncAgenda(
        deletedEventIds: List<String>,
        deletedTaskIds: List<String>,
        deletedReminderIds: List<String>
    ): Result<Unit> = safeSuspendCall {
        api.syncAgenda(
            SyncAgendaRequest(
                deletedEventIds = deletedEventIds,
                deletedTaskIds = deletedTaskIds,
                deletedReminderIds = deletedReminderIds
            )
        )
    }

    override suspend fun updateAgenda() = safeSuspendCall {
        val result = api.getFullAgenda()
        alarmHandler.cancelAllAlarms()
        saveAgendaItems(result)
        alarmHandler.setAlarmForFutureAgendaItems()
    }

    private suspend fun saveAgendaItems(agendaDto: AgendaDto) {
        supervisorScope {
            val jobsList = mutableListOf<Job>()
            agendaDto.events.map { eventDto ->
                eventDao.insertEvents(eventDto.toEventEntity())
                eventDto.attendees.forEach {
                    jobsList.add( launch { eventDao.insertAttendees(it.toAttendeeEntity()) })
                }
                eventDto.photos.forEach {
                    jobsList.add(
                        launch { eventDao.insertRemotePhotoEntity(it.toPhotoEntity(eventDto.id)) }
                    )
                }
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