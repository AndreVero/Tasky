package com.vero.tasky.auth.di

import com.vero.tasky.auth.data.remote.AuthApi
import com.vero.tasky.auth.data.remote.AuthApiFake
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [AuthApiModule::class]
)
object FakeAuthApiModule {

    @Provides
    @ViewModelScoped
    fun provideAuthRepository() : AuthApi {
        return AuthApiFake()
    }
}