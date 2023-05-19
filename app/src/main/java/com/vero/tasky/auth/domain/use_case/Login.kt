package com.vero.tasky.auth.domain.use_case

import com.vero.tasky.auth.domain.repository.AuthRepository
import com.vero.tasky.core.domain.model.User
import javax.inject.Inject

class Login @Inject constructor(
    private val authRepository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String) : Result<User> {
        return authRepository.login(
            email = email,
            password = password
        )
    }
}