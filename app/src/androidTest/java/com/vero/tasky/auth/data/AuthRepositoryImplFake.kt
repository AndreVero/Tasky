package com.vero.tasky.auth.data

import com.vero.tasky.auth.domain.repository.AuthRepository
import com.vero.tasky.core.domain.model.User

class AuthRepositoryImplFake : AuthRepository {

    var registerResult = Result.success(Unit)
    var loginResult = Result.success(User("", "", ""))

    override suspend fun register(email: String, password: String, fullName: String): Result<Unit> {
        return registerResult
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return loginResult
    }

    override suspend fun authenticate(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun logOut(): Result<Unit> {
        return Result.success(Unit)
    }
}