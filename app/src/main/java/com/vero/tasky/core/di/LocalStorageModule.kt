package com.vero.tasky.core.di

import com.vero.tasky.core.data.local.PreferencesImpl
import com.vero.tasky.core.domain.local.Preferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalStorageModule {

    @Binds
    @Singleton
    abstract fun bindPreferences(
        preferencesImpl: PreferencesImpl
    ) : Preferences

}