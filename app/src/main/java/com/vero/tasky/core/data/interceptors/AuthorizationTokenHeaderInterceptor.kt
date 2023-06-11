package com.vero.tasky.core.data.interceptors

import com.vero.tasky.core.domain.local.UserPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationTokenHeaderInterceptor(
    private val preferences: UserPreferences
    ): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val token = preferences.getUser()?.token
        val newRequest = if (!token.isNullOrEmpty()) {
            original
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            original.newBuilder().build()
        }


        return chain.proceed(request = newRequest)
    }
}