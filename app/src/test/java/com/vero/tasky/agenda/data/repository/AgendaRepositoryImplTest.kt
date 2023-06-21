@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vero.tasky.agenda.data.repository

import com.google.common.truth.Truth.assertThat
import com.vero.tasky.agenda.data.local.dao.*
import com.vero.tasky.agenda.data.local.entities.EventEntity
import com.vero.tasky.agenda.data.local.entities.ReminderEntity
import com.vero.tasky.agenda.data.local.entities.TaskEntity
import com.vero.tasky.agenda.data.remote.malformedAgendaResponse
import com.vero.tasky.agenda.data.remote.network.AgendaApi
import com.vero.tasky.agenda.data.remote.validAgendaResponse
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class AgendaRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var agendaRepository: AgendaRepository
    private lateinit var api: AgendaApi
    private lateinit var taskDao: TaskDao
    private lateinit var reminderDao: ReminderDao
    private lateinit var eventDao: EventDao

    private val taskEntity = TaskEntity(
        id = "",
        title = "",
        description = "",
        remindAt = 1,
        time = 1,
        isDone = false
    )

    private val reminderEntity = ReminderEntity(
        id = "",
        title = "",
        description = "",
        remindAt = 1,
        time = 1
    )

    private val eventEntity = EventEntity(
        id = "",
        title = "",
        description = null,
        remindAt = 1,
        time = 1,
        to = 1,
        host = "",
        isUserEventCreator = true
    )

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.SECONDS)
            .callTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()
        api = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .build()
            .create(AgendaApi::class.java)
        taskDao = TaskDaoFake()
        eventDao = EventDaoFake()
        reminderDao = ReminderDaoFake()
        agendaRepository = AgendaRepositoryImpl(
            api = api,
            taskDao = taskDao,
            eventDao = eventDao,
            reminderDao = reminderDao
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Load agenda items, return all values`() = runTest {
        taskDao.insertTasks(taskEntity)
        eventDao.insertEvents(eventEntity)
        reminderDao.insertReminders(reminderEntity)
        val items = agendaRepository.getAgendaForDay(1).toList()
        assertThat(items.size).isEqualTo(3)
    }

    @Test
    fun `Update agenda for day, valid response, result successfully saved`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validAgendaResponse)
        )

        agendaRepository.updateAgendaForDay(1)
        val tasks = taskDao.loadTasksForDay(1).toList()
        assertThat(tasks.size).isEqualTo(1)

        val events = eventDao.loadEventsForDay(1).toList()
        assertThat(events.size).isEqualTo(1)

        val reminders = reminderDao.loadRemindersForDay(1).toList()
        assertThat(reminders.size).isEqualTo(1)
    }

    @Test
    fun `Update agenda for day, malformed response, return failure`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(malformedAgendaResponse)
        )

        val result = agendaRepository.updateAgendaForDay(1)
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Update agenda for day, invalid response, return failure`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(422)
                .setBody(validAgendaResponse)
        )

        val result = agendaRepository.updateAgendaForDay(1)
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Get full agenda, valid response, result successfully saved`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validAgendaResponse)
        )

        agendaRepository.getFullAgenda()
        val tasks = taskDao.loadTasksForDay(1).toList()
        assertThat(tasks.size).isEqualTo(1)

        val events = eventDao.loadEventsForDay(1).toList()
        assertThat(events.size).isEqualTo(1)

        assertThat(events.first().first().attendees).isNotEmpty()
        assertThat(events.first().first().networkPhotos).isNotEmpty()

        val reminders = reminderDao.loadRemindersForDay(1).toList()
        assertThat(reminders.size).isEqualTo(1)

    }

    @Test
    fun `Get full agenda, malformed response, return failure`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(malformedAgendaResponse)
        )

        val result = agendaRepository.updateAgendaForDay(1)
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Get full agenda, invalid response, return failure`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(422)
                .setBody(validAgendaResponse)
        )

        val result = agendaRepository.updateAgendaForDay(1)
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Sync agenda, invalid response, return failure`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(422)
        )

        val result = agendaRepository.syncAgenda(
            emptyList(), emptyList(), emptyList()
        )
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Sync agenda, valid response, return success`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
        )

        val result = agendaRepository.syncAgenda(
            emptyList(), emptyList(), emptyList()
        )
        assertThat(result.isSuccess).isTrue()
    }
}