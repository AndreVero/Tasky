package com.vero.tasky.auth.di

import com.vero.tasky.auth.data.matcher.EmailMatcherImpl
import com.vero.tasky.auth.domain.matcher.EmailMatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object MatchersModule {

    @Provides
    @ViewModelScoped
    fun provideEmailMatcher() : EmailMatcher {
        return EmailMatcherImpl()
    }

}