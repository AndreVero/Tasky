@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vero.tasky.auth.presentation.registration

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vero.tasky.auth.data.MainCoroutineRule
import com.vero.tasky.auth.data.repository.AuthRepositoryFake
import com.vero.tasky.auth.domain.repository.AuthRepository
import com.vero.tasky.auth.domain.usecase.RegisterUseCase
import com.vero.tasky.auth.domain.usecase.RegistrationUseCases
import com.vero.tasky.core.domain.usecase.ValidateEmailUseCase
import com.vero.tasky.auth.domain.usecase.password.PasswordValidationResult
import io.mockk.every
import io.mockk.mockk
import com.vero.tasky.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegistrationViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var authRepository: AuthRepository

    companion object {
        private const val VALID_PASSWORD = "1234QWertyu"
        private const val VALID_NAME = "test23"
        private const val TOO_SHORT_PASSWORD = ""
        private const val TOO_SHORT_NAME = ""
    }

    @Before
    fun setUp() {
        authRepository = AuthRepositoryFake()
        viewModel = RegistrationViewModel(
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    "state" to RegistrationState(
                        isLoading = false,
                        isEmailValid = true,
                        isNameValid = true,
                        name = VALID_NAME,
                        password = VALID_PASSWORD,
                        emailAddress = "test@gmail.com"
                    )
                )
            ),
            registrationUseCases = RegistrationUseCases(
                validatePasswordUseCase = mockk(relaxed = true) {
                    every { invoke(password = VALID_PASSWORD) } returns PasswordValidationResult.SUCCESS
                    every { invoke(password = TOO_SHORT_PASSWORD) } returns PasswordValidationResult.TOO_SHORT
                },
                validateEmailUseCase = ValidateEmailUseCase(emailMatcher = mockk(relaxed = true)),
                validateNameUseCase =  mockk(relaxed = true) {
                    every { this@mockk(name = VALID_NAME) } returns true
                    every {  this@mockk(name = TOO_SHORT_NAME) } returns false
                },
                registerUseCase = RegisterUseCase(authRepository)
            )
        )
    }

    @Test
    fun `Register, correct info, return results`() = runTest {
        viewModel.onEvent(RegistrationEvent.SignUp)
        viewModel.uiEvent.test {
            val item = awaitItem()
            Truth.assertThat(item).isEqualTo(UiRegistrationEvent.OnSignUp)
        }
    }

    @Test
    fun `Register, email isn't valid, return error`() = runTest {
        viewModel.onEvent(RegistrationEvent.OnEmailUpdated(""))
        viewModel.onEvent(RegistrationEvent.SignUp)
        Truth.assertThat(viewModel.state.isErrorEmail).isTrue()
    }

    @Test
    fun `Register, name isn't valid, return error`() = runTest {
        viewModel.onEvent(RegistrationEvent.OnNameUpdated(TOO_SHORT_NAME))
        viewModel.onEvent(RegistrationEvent.SignUp)
        Truth.assertThat(viewModel.state.isErrorName).isTrue()
    }

    @Test
    fun `Register, password isn't valid, return error`() = runTest {
        viewModel.onEvent(RegistrationEvent.OnPasswordUpdated(TOO_SHORT_PASSWORD))
        viewModel.onEvent(RegistrationEvent.SignUp)
        Truth.assertThat(viewModel.state.isErrorPassword).isTrue()
    }

    @Test
    fun `Register, email, password and name aren't valid, return error`() = runTest {
        viewModel.onEvent(RegistrationEvent.OnEmailUpdated(""))
        viewModel.onEvent(RegistrationEvent.OnNameUpdated(TOO_SHORT_NAME))
        viewModel.onEvent(RegistrationEvent.OnPasswordUpdated(TOO_SHORT_PASSWORD))
        viewModel.onEvent(RegistrationEvent.SignUp)
        Truth.assertThat(viewModel.state.isErrorEmail).isTrue()
        Truth.assertThat(viewModel.state.isErrorPassword).isTrue()
        Truth.assertThat(viewModel.state.isErrorName).isTrue()
    }

    @Test
    fun `Register, failure response from repository, return error`() = runTest {
        (authRepository as AuthRepositoryFake).resultRegister = Result.failure(Throwable("Test"))
        viewModel.onEvent(RegistrationEvent.SignUp)

        viewModel.uiEvent.test {
            val item = awaitItem()
            Truth.assertThat((item as UiRegistrationEvent.ShowErrorMessage).message)
                .isEqualTo(R.string.network_error_on_registration)
        }
    }
}