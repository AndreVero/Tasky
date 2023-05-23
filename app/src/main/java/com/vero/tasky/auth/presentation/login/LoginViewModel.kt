package com.vero.tasky.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.tasky.auth.domain.usecase.LoginUseCases
import com.vero.tasky.auth.domain.usecase.password.PasswordValidationResult
import com.vero.tasky.core.domain.local.UserPreferences
import com.vero.tasky.core.presentation.ErrorType
import com.vero.tasky.core.presentation.UIEvent
import com.vero.tasky.core.presentation.navigation.Screens
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

    private val channel = Channel<UIEvent>()
    val uiEvent = channel.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            LoginEvent.LogIn -> if (state.isEmailValid) logIn()
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
            LoginEvent.OnPasswordVisibilityUpdate -> {
                updateState(state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                ))
            }
            LoginEvent.SignUp -> signUp()
        }
    }

    private fun logIn() {
        val password = state.password
        when (loginUseCases.validatePasswordUseCase(password)) {
            PasswordValidationResult.SUCCESS -> {
                updateState(state.copy(isLoading = true))
                viewModelScope.launch {
                    loginUseCases.loginUseCase(
                        email = state.emailAddress,
                        password = password
                    ).onSuccess { user ->
                        userPreferences.saveUser(user)
                        channel.send(UIEvent.NavigateTo(Screens.Agenda.route))
                    }.onFailure {
                        updateState(state.copy(isLoading = false))
                        showError(ErrorType.NetworkErrorOnLogin)
                    }
                }
            }
            PasswordValidationResult.TOO_SHORT -> { showError(ErrorType.PasswordIsTooShow) }
            PasswordValidationResult.NOT_SECURE -> { showError(ErrorType.PasswordIsNotSecure) }
        }
    }

    private fun showError(errorType: ErrorType) {
        viewModelScope.launch {
            channel.send(UIEvent.ShowErrorMessage(errorType))
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            channel.send(UIEvent.NavigateTo(route = Screens.SignUp.route))
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