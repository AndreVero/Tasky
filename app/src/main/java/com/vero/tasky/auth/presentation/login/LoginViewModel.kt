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
import com.vero.tasky.auth.util.PasswordErrorParser
import com.vero.tasky.auth.util.PasswordParsedResult
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
            LoginEvent.LogIn -> validateUserInfo()
            is LoginEvent.OnEmailUpdated -> {
                val email = event.email
                updateState(state.copy(
                    emailAddress = email,
                    isEmailValid = loginUseCases.validateEmailUseCase(email)
                ))
            }
            is LoginEvent.OnPasswordUpdated -> {
                updateState(state.copy(
                    password = event.password
                ))
            }
            LoginEvent.SignUp -> signUp()
        }
    }

    private fun validateUserInfo() {
        if (state.isEmailValid) {
            validatePassword()
        } else {
            showError(R.string.email_not_valid)
        }
    }

    private fun validatePassword() {
        val validationResult = loginUseCases.validatePasswordUseCase(state.password)
        val parsedResult = PasswordErrorParser.parse(validationResult)

        when (parsedResult) {
            is PasswordParsedResult.Invalid -> showError(parsedResult.error)
            PasswordParsedResult.Valid -> logIn()
        }
    }

    private fun logIn() {
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