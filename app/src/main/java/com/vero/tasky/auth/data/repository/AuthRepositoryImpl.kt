package com.vero.tasky.auth.data.repository

import com.vero.tasky.auth.data.dto.toUser
import com.vero.tasky.auth.data.network.AuthApi
import com.vero.tasky.auth.data.request.LoginRequest
import com.vero.tasky.auth.data.request.RegisterRequest
import com.vero.tasky.auth.domain.repository.AuthRepository
import com.vero.tasky.core.domain.model.User

class AuthRepositoryImpl(
    private val api : AuthApi
) : AuthRepository {

    override suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Result<Unit> {
        return try {
            api.register(
                RegisterRequest(
                    fullName = fullName,
                    password = password,
                    email = email
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            Result.success(api.login(
                LoginRequest(
                    email = email,
                    password = password
                )
            ).toUser())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun authenticate(): Result<Unit> {
        return try {
            api.authenticate()
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}