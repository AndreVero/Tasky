package com.vero.tasky.auth.data.network

import com.vero.tasky.auth.data.dto.UserLoginDto
import com.vero.tasky.auth.data.request.LoginRequest
import com.vero.tasky.auth.data.request.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ) : Response<Void>

    @GET("authenticate")
    suspend fun authenticate() : Response<Void>

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : UserLoginDto
}