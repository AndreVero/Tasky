package com.vero.tasky.agenda.data.repository

import com.google.common.truth.Truth
import com.vero.tasky.agenda.data.alarmhandler.AlarmHandlerFake
import com.vero.tasky.agenda.data.local.dao.*
import com.vero.tasky.agenda.data.mappers.toTaskEntity
import com.vero.tasky.agenda.data.remote.network.api.TaskApi
import com.vero.tasky.agenda.data.remote.validTaskResponse
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.agenda.domain.repository.TaskRepository
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class TaskRepositoryImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: TaskApi
    private lateinit var taskDao: TaskDao
    private lateinit var modifiedAgendaItemDao: ModifiedAgendaItemDao

    private lateinit var taskRepository: TaskRepository
    private lateinit var alarmHandler: AlarmHandler

    private val task = AgendaItem.Task(
        id = "1",
        title = "",
        description = null,
        remindAt = LocalDateTime.now(),
        time = LocalDateTime.now(),
        isDone = false
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
            .create(TaskApi::class.java)
        taskDao = TaskDaoFake()
        alarmHandler = AlarmHandlerFake()
        modifiedAgendaItemDao = ModifiedAgendaItemDaoFake()
        taskRepository = TaskRepositoryImpl(
            alarmHandler = alarmHandler,
            api = api,
            taskDao = taskDao,
            modifiedAgendaItemDao = modifiedAgendaItemDao,
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Save task, task saved successfully`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validTaskResponse)
        )

        taskRepository.saveTask(
            task = task,
            modificationType = ModificationType.CREATED
        )
        Truth.assertThat(taskDao.loadAllTasks().size).isEqualTo(1)
    }

    @Test
    fun `Create task, task wasn't upload network error, saved to modified table`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(422)
        )

        taskRepository.saveTask(
            task = task,
            modificationType = ModificationType.CREATED
        )
        Truth.assertThat(modifiedAgendaItemDao.loadCreatedAgendaItems().size).isEqualTo(1)
    }

    @Test
    fun `Update task, task wasn't upload network error, saved to modified table`() = runTest {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(422)
            )

            taskRepository.saveTask(
                task = task,
                modificationType = ModificationType.UPDATED
            )
            Truth.assertThat(modifiedAgendaItemDao.loadUpdatedAgendaItems().size).isEqualTo(1)
        }

    @Test
    fun `Delete task, task deleted successfully`() = runTest {
        taskDao.insertTasks(task.toTaskEntity())
        taskRepository.deleteTask(task = task)
        Truth.assertThat(taskDao.loadAllTasks().size).isEqualTo(0)
    }

    @Test
    fun `Save task, task wasn't deleted network error, saved to modified table`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(422)
        )
        taskDao.insertTasks(task.toTaskEntity())
        taskRepository.deleteTask(task = task)
        Truth.assertThat(modifiedAgendaItemDao.loadDeletedAgendaItems().size).isEqualTo(1)
    }
}