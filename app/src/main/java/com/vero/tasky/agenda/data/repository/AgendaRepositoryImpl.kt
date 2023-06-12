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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
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
    ) : Flow<List<AgendaItem>> {

        val taskFlow = taskDao.loadTasksForDay(timestamp).map {
            it.map { taskEntity ->  taskEntity.toTask() }
        }
        val eventFlow = eventDao.loadEventsForDay(timestamp).map {
            it.map { eventEntity ->  eventEntity.toEvent() }
        }
        val reminderFlow = reminderDao.loadRemindersForDay(timestamp).map {
            it.map { reminderEntity ->  reminderEntity.toReminder() }
        }
        return merge(taskFlow, eventFlow, reminderFlow)
    }

    override suspend fun updateAgendaForDay(timezone: String,
                                            timestamp: Long): Result<Unit> {
        return safeApiCall {
            val networkAgendaItems = api.getAgendaForDay(timezone, timestamp)
            saveAgendaItems(networkAgendaItems)
        }
    }

    override suspend fun syncAgenda(
        deletedEventIds: List<String>,
        deletedTaskIds: List<String>,
        deletedReminderIds: List<String>
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

    override suspend fun logOut() = safeApiCall {
        api.logOut()
    }

    private suspend fun saveAgendaItems(agendaDto: AgendaDto) {
        supervisorScope {
            val jobsList = mutableListOf<Job>()
            agendaDto.events.map { eventDto ->
                eventDto.attendees.forEach {
                    jobsList.add( launch { eventDao.insertAttendees(it.toAttendeeEntity()) })
                }
                eventDto.photos.forEach {
                    jobsList.add(
                        launch { eventDao.insertRemotePhotoEntity(it.toPhotoEntity(eventDto.id)) }
                    )
                }
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