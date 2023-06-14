package com.vero.tasky.auth.domain.repository

import com.vero.tasky.core.domain.model.User

interface AuthRepository {

    suspend fun register(email: String, password: String, fullName: String) : Result<Unit>

    suspend fun login(email: String, password: String) : Result<User>

    suspend fun authenticate() : Result<Unit>

    suspend fun logOut() : Result<Unit>
}