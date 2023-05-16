package com.vero.tasky.core.interceptors

import com.vero.tasky.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val newRequest = original
            .newBuilder()
            .addHeader("x-api-key", BuildConfig.API_KEY)
            .build()

        return chain.proceed(request = newRequest)
    }
}