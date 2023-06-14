package com.vero.tasky.auth.domain.usecase

import com.vero.tasky.auth.domain.repository.AuthRepository
import javax.inject.Inject

data class LogOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() : Result<Unit> {
        return authRepository.logOut()
    }
}