package com.vero.tasky.auth.di

import com.vero.tasky.auth.data.matcher.EmailMatcherImpl
import com.vero.tasky.auth.data.remote.AuthApi
import com.vero.tasky.auth.data.repository.AuthRepositoryImpl
import com.vero.tasky.auth.domain.matcher.EmailMatcher
import com.vero.tasky.auth.domain.repository.AuthRepository
import com.vero.tasky.auth.domain.usecase.*
import com.vero.tasky.auth.domain.usecase.password.ValidatePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    @Provides
    @ViewModelScoped
    fun provideEmailMatcher() : EmailMatcher {
        return EmailMatcherImpl()
    }

    @Provides
    @ViewModelScoped
    fun provideAuthApi(retrofit: Retrofit) : AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideAuthRepository(api: AuthApi) : AuthRepository {
        return AuthRepositoryImpl(api)
    }

    @Provides
    @ViewModelScoped
    fun provideLoginUseCases(
        emailMatcher: EmailMatcher,
        authRepository: AuthRepository,
    ) : LoginUseCases {
        return LoginUseCases(
            validateEmailUseCase = ValidateEmailUseCase(emailMatcher),
            validatePasswordUseCase = ValidatePasswordUseCase(),
            loginUseCase = LoginUseCase(authRepository)
        )
    }

    @Provides
    @ViewModelScoped
    fun provideRegisterUseCases(
        emailMatcher: EmailMatcher,
        authRepository: AuthRepository,
    ) : RegistrationUseCases {
        return RegistrationUseCases(
            validateEmailUseCase = ValidateEmailUseCase(emailMatcher),
            validatePasswordUseCase = ValidatePasswordUseCase(),
            validateNameUseCase = ValidateNameUseCase(),
            registerUseCase = RegisterUseCase(authRepository)
        )
    }
}