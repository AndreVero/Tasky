package com.vero.tasky.core.data.interceptors

import com.vero.tasky.core.domain.util.eventbus.LogOutEventBus
import com.vero.tasky.core.domain.util.eventbus.LogOutEventBusEvent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnauthorizedInterceptor @Inject constructor(
    private val eventBus: LogOutEventBus
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code == 401) {
            runBlocking {
                eventBus.sendEvent(LogOutEventBusEvent.UnauthorizedException)
            }
        }
        return response
    }
}