package com.vero.tasky.auth.presentation.login

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.tasky.R
import com.vero.tasky.auth.domain.usecase.LoginUseCases
import com.vero.tasky.auth.domain.util.PasswordErrorParser
import com.vero.tasky.auth.domain.util.ValidationResult
import com.vero.tasky.core.domain.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userPreferences: UserPreferences,
    private val loginUseCases: LoginUseCases
    ) : ViewModel()
{
    var state by mutableStateOf(
        (savedStateHandle.get(STATE_KEY) as? LoginState)?.copy(
            isLoading = false
        ) ?: LoginState()
    )
        private set

    private val channel = Channel<UiLoginEvent>()
    val uiEvent = channel.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            LoginEvent.LogIn -> logIn()
            is LoginEvent.OnEmailUpdated -> {
                val email = event.email
                updateState(state.copy(
                    emailAddress = email,
                    isEmailValid = loginUseCases.validateEmailUseCase(email),
                    isErrorEmail = false,
                    emailLabel = R.string.email_hint
                ))
            }
            is LoginEvent.OnPasswordUpdated -> {
                updateState(state.copy(
                    password = event.password,
                    isErrorPassword = false,
                    passwordLabel = R.string.password
                ))
            }
            LoginEvent.SignUp -> signUp()
        }
    }

    private fun logIn() {
        val validationResult = validatePassword()

        if (state.isEmailValid && validationResult is ValidationResult.Valid) {
            updateState(state.copy(isLoading = true))
            viewModelScope.launch {
                loginUseCases.loginUseCase(
                    email = state.emailAddress,
                    password = state.password
                ).onSuccess { user ->
                    userPreferences.saveUser(user)
                    channel.send(UiLoginEvent.OnLogIn)
                }.onFailure {
                    updateState(state.copy(isLoading = false))
                    showError(R.string.network_error_on_login)
                }
            }
        } else {
            showTextFieldsErrors(validationResult)
        }
    }

    private fun showTextFieldsErrors(validationResult: ValidationResult) {
        when (validationResult) {
            ValidationResult.Valid -> Unit
            is ValidationResult.Invalid -> {
                updateState(state.copy(
                    isErrorPassword = true,
                    passwordLabel = validationResult.error)
                )
            }
        }
        if (!state.isEmailValid) {
            updateState(state.copy(
                isErrorEmail = true,
                emailLabel = R.string.email_not_valid)
            )
        }
    }

    private fun validatePassword() : ValidationResult {
        val validationResult = loginUseCases.validatePasswordUseCase(state.password)
        return PasswordErrorParser.parse(validationResult)
    }

    private fun showError(@StringRes message: Int) {
        viewModelScope.launch {
            channel.send(UiLoginEvent.ShowErrorMessage(message))
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            channel.send(UiLoginEvent.OnSignUp)
        }
    }

    private fun updateState(newState: LoginState) {
        state = newState
        savedStateHandle.set(STATE_KEY, newState)
    }

    companion object {
        private const val STATE_KEY = "state"
    }
}