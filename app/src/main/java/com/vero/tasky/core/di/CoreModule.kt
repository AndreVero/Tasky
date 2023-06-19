package com.vero.tasky.core.di

import com.vero.tasky.BuildConfig
import com.vero.tasky.core.data.interceptors.ApiKeyHeaderInterceptor
import com.vero.tasky.core.data.interceptors.AuthorizationTokenHeaderInterceptor
import com.vero.tasky.core.data.interceptors.UnauthorizedInterceptor
import com.vero.tasky.core.domain.local.UserPreferences
import com.vero.tasky.core.domain.util.eventbus.AuthEventBus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        preferences: UserPreferences,
        unauthorizedInterceptor: UnauthorizedInterceptor
    ) : OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyHeaderInterceptor())
            .addInterceptor(AuthorizationTokenHeaderInterceptor(preferences))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(unauthorizedInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideLogOutEventBus() : AuthEventBus {
        return AuthEventBus()
    }

}