package com.vero.tasky.auth.domain.usecase

import com.vero.tasky.auth.domain.usecase.password.ValidatePasswordUseCase
import com.vero.tasky.core.domain.usecase.ValidateEmailUseCase

data class LoginUseCases(
    val validatePasswordUseCase: ValidatePasswordUseCase,
    val validateEmailUseCase: ValidateEmailUseCase,
    val loginUseCase: LoginUseCase
)