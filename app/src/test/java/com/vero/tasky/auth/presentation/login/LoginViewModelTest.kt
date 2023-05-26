@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vero.tasky.auth.presentation.login

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.vero.tasky.auth.data.MainCoroutineRule
import com.vero.tasky.auth.data.repository.AuthRepositoryFake
import com.vero.tasky.auth.domain.usecase.LoginUseCase
import com.vero.tasky.auth.domain.usecase.LoginUseCases
import com.vero.tasky.auth.domain.usecase.ValidateEmailUseCase
import com.vero.tasky.auth.domain.usecase.password.PasswordValidationResult
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

    @Before
    fun setUp() {
        viewModel = LoginViewModel(
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    "state" to LoginState(
                        isLoading = false,
                        isPasswordVisible = false ,
                        isEmailValid = true,
                        password = "123",
                        emailAddress = "123"
                    )
                )
            ),
            userPreferences = mockk(relaxed = true),
            loginUseCases = LoginUseCases(
                validatePasswordUseCase = mockk(relaxed = true) {
                    every { invoke(password = any()) } returns PasswordValidationResult.SUCCESS },
                validateEmailUseCase = ValidateEmailUseCase(emailMatcher = mockk(relaxed = true)),
                loginUseCase = LoginUseCase(AuthRepositoryFake())
            )
        )
    }

    @Test
    fun `Login, correct info, return results`() = runTest {
        viewModel.onEvent(LoginEvent.LogIn)
        viewModel.uiEvent.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(UiLoginEvent.OnLogIn)
        }
    }
}