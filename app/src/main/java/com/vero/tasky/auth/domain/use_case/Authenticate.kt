package com.vero.tasky.auth.domain.use_case

import com.vero.tasky.auth.domain.repository.AuthRepository
import javax.inject.Inject

class Authenticate @Inject constructor(
    private val authRepository: AuthRepository) {

    suspend operator fun invoke() : Result<Unit> {
        return authRepository.authenticate()
    }
}