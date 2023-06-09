package com.vero.tasky.auth.data.repository

import com.vero.tasky.auth.data.remote.dto.toUser
import com.vero.tasky.auth.data.remote.AuthApi
import com.vero.tasky.auth.data.remote.request.LoginRequest
import com.vero.tasky.auth.data.remote.request.RegisterRequest
import com.vero.tasky.auth.domain.repository.AuthRepository
import com.vero.tasky.core.data.remote.safeApiCall
import com.vero.tasky.core.domain.model.User

class AuthRepositoryImpl(
    private val api : AuthApi
) : AuthRepository{

    override suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Result<Unit> {
        return safeApiCall { api.register(
            RegisterRequest(
                fullName = fullName,
                password = password,
                email = email
            )
        ) }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return safeApiCall {
            api.login(
                LoginRequest(
                    email = email,
                    password = password
                )
            ).toUser()
        }
    }

    override suspend fun authenticate(): Result<Unit> {
        return safeApiCall {
            api.authenticate()
        }
    }

}