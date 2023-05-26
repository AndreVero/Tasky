@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vero.tasky.auth.data.repository

import com.google.common.truth.Truth.assertThat
import com.vero.tasky.auth.data.remote.AuthApi
import com.vero.tasky.auth.data.remote.malformedLoginResponse
import com.vero.tasky.auth.data.remote.validLoginResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class AuthRepositoryImplTest {

    private lateinit var repository: AuthRepositoryImpl
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: AuthApi

    companion object {
        private const val DEFAULT_PASSWORD = "1234Tr1234"
        private const val DEFAULT_EMAIL = "test@gmail.com"
        private const val DEFAULT_NAME = "test"
    }
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.SECONDS)
            .callTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()
        api = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .build()
            .create(AuthApi::class.java)
        repository = AuthRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Login, valid response, return results`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validLoginResponse)
        )

        val result = repository.login(DEFAULT_EMAIL, DEFAULT_PASSWORD)
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `Login, invalid response, return failure`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
        )

        val result = repository.login(DEFAULT_EMAIL, DEFAULT_PASSWORD)
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Login, malformed response, return results`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(malformedLoginResponse)
        )

        val result = repository.login(DEFAULT_EMAIL, DEFAULT_PASSWORD)
        assertThat(result.isSuccess).isFalse()
    }

    @Test
    fun `Register, valid response, return results`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validLoginResponse)
        )

        val result = repository.register(DEFAULT_EMAIL, DEFAULT_PASSWORD, DEFAULT_NAME)
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `Register, invalid response, return failure`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(422)
        )

        val result = repository.register(DEFAULT_EMAIL, DEFAULT_PASSWORD, DEFAULT_NAME)
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Authenticate, valid response, return results`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
        )

        val result = repository.authenticate()
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `Authenticate, invalid response, return failure`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(401)
        )

        val result = repository.authenticate()
        assertThat(result.isFailure).isTrue()
    }

}