package com.vero.tasky.auth.domain.use_case

import com.vero.tasky.auth.domain.repository.AuthRepository
import javax.inject.Inject

class Register @Inject constructor(
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