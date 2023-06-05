package com.vero.tasky.agenda.di

import com.vero.tasky.agenda.data.remote.network.AgendaApi
import com.vero.tasky.agenda.data.remote.repository.AgendaRepositoryImpl
import com.vero.tasky.agenda.domain.repository.AgendaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideAgendaRepository(api: AgendaApi) : AgendaRepository {
        return AgendaRepositoryImpl(api)
    }
}