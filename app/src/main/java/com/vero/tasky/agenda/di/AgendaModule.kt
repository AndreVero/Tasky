package com.vero.tasky.agenda.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.vero.tasky.agenda.data.local.AgendaDatabase
import com.vero.tasky.agenda.data.local.dao.DeletedEventsDao
import com.vero.tasky.agenda.data.local.dao.DeletedRemindersDao
import com.vero.tasky.agenda.data.local.dao.DeletedTasksDao
import com.vero.tasky.agenda.data.remote.network.AgendaApi
import com.vero.tasky.agenda.data.repository.AgendaRepositoryImpl
import com.vero.tasky.agenda.data.workmanagerrunner.GetFullAgendaWorkerRunnerImpl
import com.vero.tasky.agenda.data.workmanagerrunner.SyncAgendaWorkerRunnerImpl
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import com.vero.tasky.agenda.domain.usecase.AgendaUseCases
import com.vero.tasky.agenda.domain.usecase.GetAgendaForDayUseCase
import com.vero.tasky.agenda.domain.usecase.UpdateAgendaForDayUseCase
import com.vero.tasky.agenda.domain.workmanagerrunner.GetFullAgendaWorkerRunner
import com.vero.tasky.agenda.domain.workmanagerrunner.SyncAgendaWorkerRunner
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
    fun provideAgendaRepository(api: AgendaApi, db: AgendaDatabase) : AgendaRepository {
        return AgendaRepositoryImpl(
            api = api,
            taskDao = db.taskDao(),
            eventDao = db.eventDao(),
            reminderDao = db.reminderDao()
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
    fun provideGetFullAgendaWorkManagerRunner(@ApplicationContext context: Context)
        : GetFullAgendaWorkerRunner {
        return GetFullAgendaWorkerRunnerImpl(
            workManager = WorkManager.getInstance(context)
        )
    }

    @Provides
    @Singleton
    fun provideSyncAgendaWorkManagerRunner(@ApplicationContext context: Context)
        : SyncAgendaWorkerRunner {
        return SyncAgendaWorkerRunnerImpl(
            workManager = WorkManager.getInstance(context)
        )
    }

    @Provides
    @Singleton
    fun provideDeletedTasksDao(db: AgendaDatabase) : DeletedTasksDao {
        return db.deletedTasksDao()
    }

    @Provides
    @Singleton
    fun provideDeletedEventsDao(db: AgendaDatabase) : DeletedEventsDao {
        return db.deletedEventsDao()
    }

    @Provides
    @Singleton
    fun provideDeletedRemindersDao(db: AgendaDatabase) : DeletedRemindersDao {
        return db.deletedRemindersDao()
    }

    @Provides
    @Singleton
    fun provideAgendaUseCases(agendaRepository: AgendaRepository) : AgendaUseCases {
        return AgendaUseCases(
            getAgendaForDayUseCase = GetAgendaForDayUseCase(agendaRepository),
            updateAgendaForDayUseCase = UpdateAgendaForDayUseCase(agendaRepository)
        )
    }
}