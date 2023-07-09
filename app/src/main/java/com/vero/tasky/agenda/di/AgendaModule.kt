package com.vero.tasky.agenda.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.squareup.moshi.Moshi
import com.vero.tasky.agenda.data.local.AgendaDatabase
import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.alarmhandler.AlarmHandlerImpl
import com.vero.tasky.agenda.data.local.dao.ReminderDao
import com.vero.tasky.agenda.data.local.dao.TaskDao
import com.vero.tasky.agenda.data.remote.network.api.AgendaApi
import com.vero.tasky.agenda.data.remote.network.api.EventApi
import com.vero.tasky.agenda.data.remote.network.api.TaskApi
import com.vero.tasky.agenda.data.repository.AgendaRepositoryImpl
import com.vero.tasky.agenda.data.repository.EventRepositoryImpl
import com.vero.tasky.agenda.data.repository.TaskRepositoryImpl
import com.vero.tasky.agenda.data.util.FileCompressor
import com.vero.tasky.agenda.data.util.multipart.MultipartParser
import com.vero.tasky.agenda.data.workmanagerrunner.UpdateAgendaRunnerImpl
import com.vero.tasky.agenda.data.workmanagerrunner.SyncAgendaRunnerImpl
import com.vero.tasky.agenda.data.workmanagerrunner.SaveEventRunnerImpl
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import com.vero.tasky.agenda.domain.repository.EventRepository
import com.vero.tasky.agenda.domain.repository.TaskRepository
import com.vero.tasky.agenda.domain.usecase.AgendaUseCases
import com.vero.tasky.agenda.domain.usecase.GetAgendaForDayUseCase
import com.vero.tasky.agenda.domain.usecase.SyncAgendaUseCase
import com.vero.tasky.agenda.domain.usecase.UpdateAgendaForDayUseCase
import com.vero.tasky.agenda.domain.usecase.event.*
import com.vero.tasky.agenda.domain.usecase.task.DeleteTaskUseCase
import com.vero.tasky.agenda.domain.usecase.task.GetTaskUseCase
import com.vero.tasky.agenda.domain.usecase.task.SaveTaskUseCase
import com.vero.tasky.agenda.domain.usecase.task.TaskUseCases
import com.vero.tasky.agenda.domain.workmanagerrunner.UpdateAgendaRunner
import com.vero.tasky.agenda.domain.workmanagerrunner.SyncAgendaRunner
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveEventRunner
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
        : UpdateAgendaRunner {
        return UpdateAgendaRunnerImpl(
            workManager = workManager
        )
    }

    @Provides
    @Singleton
    fun provideSyncAgendaWorkerRunner(workManager: WorkManager)
        : SyncAgendaRunner {
        return SyncAgendaRunnerImpl(
            workManager = workManager
        )
    }

    @Provides
    @Singleton
    fun provideSaveEventWorkerRunner(workManager: WorkManager)
            : SaveEventRunner {
        return SaveEventRunnerImpl(
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
    fun provideTaskDao(db: AgendaDatabase) : TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun provideReminderDao(db: AgendaDatabase) : ReminderDao {
        return db.reminderDao()
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
    fun provideTaskApi(retrofit: Retrofit) : TaskApi {
        return retrofit.create(TaskApi::class.java)
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
        saveEventRunner: SaveEventRunner,
        alarmHandler: AlarmHandler,
        syncAgendaUseCase: SyncAgendaUseCase,
    ) : EventRepository {
        return EventRepositoryImpl(
            api = api,
            eventDao = db.eventDao(),
            modifiedAgendaItemDao = db.modifiedAgendaItemDao(),
            multipartParser = multipartParser,
            saveEventRunner = saveEventRunner,
            alarmHandler = alarmHandler,
            syncAgendaUseCase = syncAgendaUseCase
        )
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        api: TaskApi,
        db: AgendaDatabase,
        alarmHandler: AlarmHandler,
    ) : TaskRepository {
        return TaskRepositoryImpl(
            api = api,
            taskDao = db.taskDao(),
            modifiedAgendaItemDao = db.modifiedAgendaItemDao(),
            alarmHandler = alarmHandler,
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
    fun provideTaskUseCases(
        taskRepository: TaskRepository
    ) : TaskUseCases {
        return TaskUseCases(
            getTask = GetTaskUseCase(taskRepository),
            saveTask = SaveTaskUseCase(taskRepository),
            deleteTask = DeleteTaskUseCase(taskRepository)
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