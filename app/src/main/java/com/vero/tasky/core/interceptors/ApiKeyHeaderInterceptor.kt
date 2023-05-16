package com.vero.tasky.core.interceptors

import com.vero.tasky.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiKeyHeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val newRequest = original
            .newBuilder()
            .addHeader(API_KEY_HEADER, BuildConfig.API_KEY)
            .build()

        return chain.proceed(request = newRequest)
    }

    companion object {
        private const val API_KEY_HEADER = "x-api-key"
    }
}