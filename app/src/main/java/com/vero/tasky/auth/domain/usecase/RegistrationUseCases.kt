package com.vero.tasky.auth.domain.usecase

import com.vero.tasky.auth.domain.usecase.password.ValidatePasswordUseCase
import com.vero.tasky.core.domain.usecase.ValidateEmailUseCase

data class RegistrationUseCases(
    val validateNameUseCase: ValidateNameUseCase,
    val validateEmailUseCase: ValidateEmailUseCase,
    val validatePasswordUseCase: ValidatePasswordUseCase,
    val registerUseCase: RegisterUseCase
)