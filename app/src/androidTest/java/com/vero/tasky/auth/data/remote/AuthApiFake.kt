package com.vero.tasky.auth.data.remote

import com.vero.tasky.auth.data.remote.dto.UserLoginDto
import com.vero.tasky.auth.data.remote.request.LoginRequest
import com.vero.tasky.auth.data.remote.request.RegisterRequest

class AuthApiFake : AuthApi {
    override suspend fun register(registerRequest: RegisterRequest) {}

    override suspend fun authenticate() {}

    override suspend fun login(loginRequest: LoginRequest): UserLoginDto {
        if (loginRequest.email == AuthTestConstants.validTestEmail)
            return UserLoginDto("1", "1", "1")
        else throw Exception()
    }

    override suspend fun logOut() {}
}