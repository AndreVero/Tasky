package com.vero.tasky.auth.data.repository

import com.vero.tasky.auth.domain.repository.AuthRepository
import com.vero.tasky.core.domain.model.User

class AuthRepositoryFake : AuthRepository {

    var resultLogin: Result<User> = Result.success(
        User("test", "1234", "1234")
    )
    var resultRegister: Result<Unit> = Result.success(Unit)


    override suspend fun register(email: String, password: String, fullName: String): Result<Unit> {
        return resultRegister
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return resultLogin
    }

    override suspend fun authenticate(): Result<Unit> {
        return Result.success(Unit)
    }
}