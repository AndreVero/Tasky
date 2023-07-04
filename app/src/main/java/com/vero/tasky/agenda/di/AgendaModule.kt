package com.vero.tasky.agenda.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.squareup.moshi.Moshi
import com.vero.tasky.agenda.data.local.AgendaDatabase
import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.remindermanager.AlarmHandlerImpl
import com.vero.tasky.agenda.data.remote.network.api.AgendaApi
import com.vero.tasky.agenda.data.remote.network.api.EventApi
import com.vero.tasky.agenda.data.repository.AgendaRepositoryImpl
import com.vero.tasky.agenda.data.repository.EventRepositoryImpl
import com.vero.tasky.agenda.data.util.FileCompressor
import com.vero.tasky.agenda.data.util.multipart.MultipartParser
import com.vero.tasky.agenda.data.workmanagerrunner.GetFullAgendaWorkerRunnerImpl
import com.vero.tasky.agenda.data.workmanagerrunner.SyncAgendaWorkerRunnerImpl
import com.vero.tasky.agenda.data.workmanagerrunner.SaveEventWorkerRunnerImpl
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import com.vero.tasky.agenda.domain.repository.EventRepository
import com.vero.tasky.agenda.domain.usecase.AgendaUseCases
import com.vero.tasky.agenda.domain.usecase.GetAgendaForDayUseCase
import com.vero.tasky.agenda.domain.usecase.UpdateAgendaForDayUseCase
import com.vero.tasky.agenda.domain.usecase.event.*
import com.vero.tasky.agenda.domain.workmanagerrunner.GetFullAgendaWorkerRunner
import com.vero.tasky.agenda.domain.workmanagerrunner.SyncAgendaWorkerRunner
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveEventWorkerRunner
import com.vero.tasky.core.domain.local.UserPreferences
import com.vero.tasky.core.domain.usecase.ValidateEmailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AgendaModule {

    @Provides
    @Singleton
    fun provideAgendaApi(retrofit: Retrofit) : AgendaApi {
        return retrofit.create(AgendaApi::class.java)
    }
    @Provides
    @Singleton
    fun provideMoshi() : Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideAgendaRepository(api: AgendaApi, db: AgendaDatabase, alarmHandler: AlarmHandler)
        : AgendaRepository {
        return AgendaRepositoryImpl(
            api = api,
            taskDao = db.taskDao(),
            eventDao = db.eventDao(),
            reminderDao = db.reminderDao(),
            alarmHandler = alarmHandler
        )
    }

    @Provides
    @Singleton
    fun provideAgendaDatabase(@ApplicationContext context: Context) : AgendaDatabase {
        return Room.databaseBuilder(
            context,
            AgendaDatabase::class.java, "agenda_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) : WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideGetFullAgendaWorkerRunner(workManager: WorkManager)
        : GetFullAgendaWorkerRunner {
        return GetFullAgendaWorkerRunnerImpl(
            workManager = workManager
        )
    }

    @Provides
    @Singleton
    fun provideSyncAgendaWorkerRunner(workManager: WorkManager)
        : SyncAgendaWorkerRunner {
        return SyncAgendaWorkerRunnerImpl(
            workManager = workManager
        )
    }

    @Provides
    @Singleton
    fun provideUpdateEventWorkerRunner(workManager: WorkManager)
            : SaveEventWorkerRunner {
        return SaveEventWorkerRunnerImpl(
            workManager = workManager
        )
    }

    @Provides
    @Singleton
    fun provideDeletedTasksDao(db: AgendaDatabase) : ModifiedAgendaItemDao {
        return db.modifiedAgendaItemDao()
    }

    @Provides
    @Singleton
    fun provideEventDao(db: AgendaDatabase) : EventDao {
        return db.eventDao()
    }

    @Provides
    @Singleton
    fun provideAgendaUseCases(agendaRepository: AgendaRepository) : AgendaUseCases {
        return AgendaUseCases(
            getAgendaForDayUseCase = GetAgendaForDayUseCase(agendaRepository),
            updateAgendaForDayUseCase = UpdateAgendaForDayUseCase(agendaRepository)
        )
    }

    @Provides
    @Singleton
    fun provideEventApi(retrofit: Retrofit) : EventApi {
        return retrofit.create(EventApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMultipartParser(
        @ApplicationContext context: Context) : MultipartParser {
        return MultipartParser(FileCompressor(context))
    }

    @Provides
    @Singleton
    fun provideEventRepository(
        api: EventApi,
        db: AgendaDatabase,
        multipartParser: MultipartParser,
        saveEventWorkerRunner: SaveEventWorkerRunner,
        alarmHandler: AlarmHandler
    ) : EventRepository {
        return EventRepositoryImpl(
            api = api,
            eventDao = db.eventDao(),
            modifiedAgendaItemDao = db.modifiedAgendaItemDao(),
            multipartParser = multipartParser,
            saveEventWorkerRunner = saveEventWorkerRunner,
            alarmHandler = alarmHandler
        )
    }

    @Provides
    @Singleton
    fun provideEventUseCases(
        eventRepository: EventRepository,
        validateEmailUseCase: ValidateEmailUseCase
    ) : EventUseCases {
        return EventUseCases(
            checkAttendee = CheckAttendeeUseCase(eventRepository),
            saveEvent = SaveEventUseCase(eventRepository),
            deleteAttendee = DeleteAttendeeUseCase(eventRepository),
            deleteEvent = DeleteEventUseCase(eventRepository),
            fetchEvent = FetchEventUseCase(eventRepository),
            getEvent = GetEventInfoUseCase(eventRepository),
            validateEmailUseCase = validateEmailUseCase
        )
    }

    @Provides
    @Singleton
    fun provideReminderManager(
        @ApplicationContext context: Context,
        db: AgendaDatabase,
        preferences: UserPreferences
    ) : AlarmHandler {
        return AlarmHandlerImpl(
            context = context,
            taskDao = db.taskDao(),
            reminderDao = db.reminderDao(),
            eventDao = db.eventDao(),
            preferences = preferences
        )
    }
}