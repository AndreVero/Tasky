package com.vero.tasky.auth.domain.usecase

import com.vero.tasky.auth.domain.repository.AuthRepository

class RegisterUseCase (
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String, fullName: String) : Result<Unit> {
        return authRepository.register(
            email = email,
            password = password,
            fullName = fullName
        )
    }
}