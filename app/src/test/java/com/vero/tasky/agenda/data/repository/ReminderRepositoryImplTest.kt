package com.vero.tasky.agenda.data.repository

import com.google.common.truth.Truth
import com.vero.tasky.agenda.data.alarmhandler.AlarmHandlerFake
import com.vero.tasky.agenda.data.local.dao.*
import com.vero.tasky.agenda.data.mappers.toReminderEntity
import com.vero.tasky.agenda.data.remote.network.api.ReminderApi
import com.vero.tasky.agenda.data.remote.validReminderResponse
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.agenda.domain.repository.ReminderRepository
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

class ReminderRepositoryImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: ReminderApi
    private lateinit var reminderDao: ReminderDao
    private lateinit var modifiedAgendaItemDao: ModifiedAgendaItemDao

    private lateinit var reminderRepository: ReminderRepository
    private lateinit var alarmHandler: AlarmHandler

    private val reminder = AgendaItem.Reminder(
        id = "1",
        title = "",
        description = null,
        remindAt = LocalDateTime.now(),
        time = LocalDateTime.now()
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
            .create(ReminderApi::class.java)
        reminderDao = ReminderDaoFake()
        alarmHandler = AlarmHandlerFake()
        modifiedAgendaItemDao = ModifiedAgendaItemDaoFake()
        reminderRepository = ReminderRepositoryImpl(
            alarmHandler = alarmHandler,
            api = api,
            reminderDao = reminderDao,
            modifiedAgendaItemDao = modifiedAgendaItemDao,
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Save reminder, reminder saved successfully`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validReminderResponse)
        )

        reminderRepository.saveReminder(
            reminder = reminder,
            modificationType = ModificationType.CREATED
        )
        Truth.assertThat(reminderDao.loadAllReminders()).hasSize(1)
    }

    @Test
    fun `Create reminder, reminder wasn't upload network error, saved to modified table`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(422)
        )

        reminderRepository.saveReminder(
            reminder = reminder,
            modificationType = ModificationType.CREATED
        )
        Truth.assertThat(modifiedAgendaItemDao.loadCreatedAgendaItems()).hasSize(1)
    }

    @Test
    fun `Update reminder, reminder wasn't upload network error, saved to modified table`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(422)
        )

        reminderRepository.saveReminder(
            reminder = reminder,
            modificationType = ModificationType.UPDATED
        )
        Truth.assertThat(modifiedAgendaItemDao.loadUpdatedAgendaItems()).hasSize(1)
    }

    @Test
    fun `Delete reminder, reminder deleted successfully`() = runTest {
        reminderDao.insertReminders(reminder.toReminderEntity())
        reminderRepository.deleteReminder(reminder)
        Truth.assertThat(reminderDao.loadAllReminders()).isEmpty()
    }

    @Test
    fun `Save reminder, reminder wasn't deleted network error, saved to modified table`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(422)
        )
        reminderDao.insertReminders(reminder.toReminderEntity())
        reminderRepository.deleteReminder(reminder)
        Truth.assertThat(modifiedAgendaItemDao.loadDeletedAgendaItems()).hasSize(1)
    }
}