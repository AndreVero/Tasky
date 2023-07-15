package com.vero.tasky.agenda.data.repository

import com.google.common.truth.Truth
import com.vero.tasky.agenda.data.alarmhandler.AlarmHandlerFake
import com.vero.tasky.agenda.data.local.dao.*
import com.vero.tasky.agenda.data.remote.malformedGetAttendeeResponse
import com.vero.tasky.agenda.data.remote.network.api.EventApi
import com.vero.tasky.agenda.data.remote.validGetAttendeeResponseUserExist
import com.vero.tasky.agenda.data.remote.validGetAttendeeResponseUserNotExist
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaPhoto
import com.vero.tasky.agenda.domain.model.Attendee
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.agenda.domain.repository.EventRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
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

class EventRepositoryImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: EventApi
    private lateinit var eventDao: EventDao
    private lateinit var modifiedAgendaItemDao: ModifiedAgendaItemDao

    private lateinit var eventRepository: EventRepository
    private lateinit var alarmHandler: AlarmHandler

    private val fakeListOfLocalPhotos = listOf(
        AgendaPhoto.LocalPhoto(""),
        AgendaPhoto.LocalPhoto(""),
    )

    private val fakeListOfAttendee = listOf(
        Attendee(
            email = "",
            fullName = "",
            userId = "",
            eventId = "1",
            isGoing = true,
            remindAt = 1
        )
    )

    private val event = AgendaItem.Event(
        id = "1",
        title = "",
        description = null,
        remindAt = LocalDateTime.now(),
        time = LocalDateTime.now(),
        to = LocalDateTime.now(),
        host = "",
        isUserEventCreator = true,
        attendees = fakeListOfAttendee,
        photos = fakeListOfLocalPhotos
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
            .create(EventApi::class.java)
        eventDao = EventDaoFake()
        alarmHandler = AlarmHandlerFake()
        modifiedAgendaItemDao = ModifiedAgendaItemDaoFake()
        eventRepository = EventRepositoryImpl(
            alarmHandler = alarmHandler,
            api = api,
            eventDao = eventDao,
            modifiedAgendaItemDao = modifiedAgendaItemDao,
            saveEventRunner = mockk(relaxed = true),
            multipartParser = mockk(relaxed = true) {
                every { getMultipartPhotos(fakeListOfLocalPhotos) } returns listOf(
                    MultipartBody.Part.createFormData("", "",)
                )
                every { getMultipartPhotos(emptyList()) } returns listOf()
            }
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Save event, one image was skipped`() = runTest {
        val result = eventRepository.saveEvent(
            event = event,
            isGoing = true,
            deletedPhotoKeys = emptyList(),
            modificationType = ModificationType.CREATED
        )
        Truth.assertThat(result.getOrNull()?.countOfSkippedMessages).isEqualTo(1)
    }

    @Test
    fun `Save event, event was saved successfully`() = runTest {
        eventRepository.saveEvent(
            event = event,
            isGoing = true,
            deletedPhotoKeys = emptyList(),
            modificationType = ModificationType.CREATED
        )
        Truth.assertThat(eventDao.loadAllEvents().size).isEqualTo(1)
    }

    @Test
    fun `Delete event, event was deleted successfully`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
        )

        eventRepository.saveEvent(
            event = event,
            isGoing = true,
            deletedPhotoKeys = emptyList(),
            modificationType = ModificationType.CREATED
        )
        eventRepository.deleteEvent(
            event = event
        )
        Truth.assertThat(eventDao.loadAllEvents().size).isEqualTo(0)
    }

    @Test
    fun `Delete event, event wasn't deleted`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(422)
        )

        eventRepository.saveEvent(
            event = event,
            isGoing = true,
            deletedPhotoKeys = emptyList(),
            modificationType = ModificationType.CREATED
        )
        eventRepository.deleteEvent(
            event = event
        )
        Truth.assertThat(modifiedAgendaItemDao.loadDeletedAgendaItems().size).isEqualTo(1)
    }

    @Test
    fun `Get attendee, attendee was added successfully, return attendee`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validGetAttendeeResponseUserExist)
        )

        val result = eventRepository.getAttendee("1")
        Truth.assertThat(result.getOrNull()).isNotNull()
    }

    @Test
    fun `Get attendee, attendee doesn't exist return null`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validGetAttendeeResponseUserNotExist)
        )

        val result = eventRepository.getAttendee("1")
        Truth.assertThat(result.getOrNull()).isNull()
    }

    @Test
    fun `Get attendee, network error, return null`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(422)
        )

        val result = eventRepository.getAttendee("1")
        Truth.assertThat(result.getOrNull()).isNull()
    }

    @Test
    fun `Get attendee, malformed response, return null`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(malformedGetAttendeeResponse)
        )

        val result = eventRepository.getAttendee("1")
        Truth.assertThat(result.getOrNull()).isNull()
    }
}