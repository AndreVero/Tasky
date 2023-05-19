package com.vero.tasky.auth.di

import com.vero.tasky.auth.data.network.AuthApi
import com.vero.tasky.auth.data.repository.AuthRepositoryImpl
import com.vero.tasky.auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit) : AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi) : AuthRepository {
        return AuthRepositoryImpl(api)
    }

}