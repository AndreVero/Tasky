package com.vero.tasky.agenda.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.vero.tasky.agenda.data.local.AgendaDatabase
import com.vero.tasky.agenda.data.local.dao.DeletedAgendaItemDao
import com.vero.tasky.agenda.data.remote.network.AgendaApi
import com.vero.tasky.agenda.data.repository.AgendaRepositoryImpl
import com.vero.tasky.agenda.data.workmanagerrunner.GetFullAgendaWorkManagerRunnerImpl
import com.vero.tasky.agenda.data.workmanagerrunner.SyncAgendaWorkManagerRunnerImpl
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import com.vero.tasky.agenda.domain.usecase.AgendaUseCases
import com.vero.tasky.agenda.domain.usecase.GetAgendaForDayUseCase
import com.vero.tasky.agenda.domain.usecase.LogOutUseCase
import com.vero.tasky.agenda.domain.usecase.UpdateAgendaForDayUseCase
import com.vero.tasky.agenda.domain.workmanagerrunner.GetFullAgendaWorkManagerRunner
import com.vero.tasky.agenda.domain.workmanagerrunner.SyncAgendaWorkManagerRunner
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
    fun provideGetFullAgendaWorkManagerRunner(workManager: WorkManager)
        : GetFullAgendaWorkManagerRunner {
        return GetFullAgendaWorkManagerRunnerImpl(
            workManager = workManager
        )
    }

    @Provides
    @Singleton
    fun provideSyncAgendaWorkManagerRunner(workManager: WorkManager)
        : SyncAgendaWorkManagerRunner {
        return SyncAgendaWorkManagerRunnerImpl(
            workManager = workManager
        )
    }

    @Provides
    @Singleton
    fun provideDeletedAgendaItemDao(db: AgendaDatabase) : DeletedAgendaItemDao {
        return db.deletedAgendaItemDao()
    }

    @Provides
    @Singleton
    fun provideAgendaUseCases(agendaRepository: AgendaRepository) : AgendaUseCases {
        return AgendaUseCases(
            getAgendaForDayUseCase = GetAgendaForDayUseCase(agendaRepository),
            updateAgendaForDayUseCase = UpdateAgendaForDayUseCase(agendaRepository),
            logOutUseCase = LogOutUseCase(agendaRepository)
        )
    }
}