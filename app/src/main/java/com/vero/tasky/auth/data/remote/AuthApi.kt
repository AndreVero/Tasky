package com.vero.tasky.auth.data.remote

import com.vero.tasky.auth.data.remote.dto.UserLoginDto
import com.vero.tasky.auth.data.remote.request.LoginRequest
import com.vero.tasky.auth.data.remote.request.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    )

    @GET("authenticate")
    suspend fun authenticate()

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : UserLoginDto
}