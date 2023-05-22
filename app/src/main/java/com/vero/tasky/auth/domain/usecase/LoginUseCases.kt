package com.vero.tasky.auth.domain.usecase

import com.vero.tasky.auth.domain.usecase.password.ValidatePasswordUseCase

data class LoginUseCases(
    val validatePasswordUseCase: ValidatePasswordUseCase,
    val validateEmailUseCase: ValidateEmailUseCase,
    val loginUseCase: LoginUseCase
)