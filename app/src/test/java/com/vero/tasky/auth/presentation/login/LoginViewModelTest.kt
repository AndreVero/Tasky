@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vero.tasky.auth.presentation.login

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.vero.tasky.R
import com.vero.tasky.auth.data.MainCoroutineRule
import com.vero.tasky.auth.data.repository.AuthRepositoryFake
import com.vero.tasky.auth.domain.repository.AuthRepository
import com.vero.tasky.auth.domain.usecase.LoginUseCase
import com.vero.tasky.auth.domain.usecase.LoginUseCases
import com.vero.tasky.auth.domain.usecase.ValidateEmailUseCase
import com.vero.tasky.auth.domain.usecase.password.PasswordValidationResult
import com.vero.tasky.core.domain.util.eventbus.AuthEventBus
import com.vero.tasky.core.domain.util.eventbus.AuthEventBusEvent
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: LoginViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var authEventBus: AuthEventBus

    companion object {
        private const val VALID_PASSWORD = "1234QWertyu"
        private const val TOO_SHORT_PASSWORD = ""
    }

    @Before
    fun setUp() {
        authEventBus = AuthEventBus()
        authRepository = AuthRepositoryFake()
        viewModel = LoginViewModel(
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    "state" to LoginState(
                        isLoading = false,
                        isEmailValid = true,
                        password = VALID_PASSWORD,
                        emailAddress = "test@gmail.com"
                    )
                )
            ),
            userPreferences = mockk(relaxed = true),
            loginUseCases = LoginUseCases(
                validatePasswordUseCase = mockk(relaxed = true) {
                    every { invoke(password = VALID_PASSWORD) } returns PasswordValidationResult.SUCCESS
                    every { invoke(password = TOO_SHORT_PASSWORD) } returns PasswordValidationResult.TOO_SHORT
                                                                },
                validateEmailUseCase = ValidateEmailUseCase(emailMatcher = mockk(relaxed = true)),
                loginUseCase = LoginUseCase(authRepository),
            ),
            authEventBus = authEventBus
        )
    }

    @Test
    fun `Login, correct info, return results`() = runTest {
        viewModel.onEvent(LoginEvent.LogIn)
        authEventBus.authFlow.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(AuthEventBusEvent.LogIn)
        }
    }

    @Test
    fun `Login, email isn't valid, return error`() = runTest {
        viewModel.onEvent(LoginEvent.OnEmailUpdated(""))
        viewModel.onEvent(LoginEvent.LogIn)
        assertThat(viewModel.state.isErrorEmail).isTrue()
    }

    @Test
    fun `Login, password isn't valid, return error`() = runTest {
        viewModel.onEvent(LoginEvent.OnPasswordUpdated(TOO_SHORT_PASSWORD))
        viewModel.onEvent(LoginEvent.LogIn)
        assertThat(viewModel.state.isErrorPassword).isTrue()
    }

    @Test
    fun `Login, email and password aren't valid, return error`() = runTest {
        viewModel.onEvent(LoginEvent.OnEmailUpdated(""))
        viewModel.onEvent(LoginEvent.OnPasswordUpdated(TOO_SHORT_PASSWORD))
        viewModel.onEvent(LoginEvent.LogIn)
        assertThat(viewModel.state.isErrorPassword).isTrue()
        assertThat(viewModel.state.isErrorEmail).isTrue()
    }

    @Test
    fun `Login, failure response from repository, return error`() = runTest {
        (authRepository as AuthRepositoryFake).resultLogin = Result.failure(Throwable("Test"))
        viewModel.onEvent(LoginEvent.LogIn)
        viewModel.uiEvent.test {
            val item = awaitItem()
            assertThat((item as UiLoginEvent.ShowErrorMessage).message)
                .isEqualTo(R.string.network_error_on_login)
        }
    }
}