package com.vero.tasky.auth.data.repository

import com.vero.tasky.auth.domain.repository.AuthRepository
import com.vero.tasky.core.domain.model.User

class AuthRepositoryFake : AuthRepository {
    override suspend fun register(email: String, password: String, fullName: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return Result.success(
            User("test", "1234", "1234")
        )
    }

    override suspend fun authenticate(): Result<Unit> {
        return Result.success(Unit)
    }
}