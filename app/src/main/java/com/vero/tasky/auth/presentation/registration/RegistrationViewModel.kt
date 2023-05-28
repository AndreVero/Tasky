package com.vero.tasky.auth.presentation.registration

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.tasky.R
import com.vero.tasky.auth.domain.usecase.RegistrationUseCases
import com.vero.tasky.auth.util.PasswordValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUseCases: RegistrationUseCases,
    private val savedStateHandle: SavedStateHandle
) :ViewModel() {

    var state : RegistrationState by mutableStateOf(
        savedStateHandle.get<RegistrationState>(STATE_KEY)?.copy(
            isLoading = false
        ) ?: RegistrationState()
    )
        private set

    private val channel = Channel<UiRegistrationEvent>()
    val uiEvent = channel.receiveAsFlow()

    fun onEvent(event: RegistrationEvent) {
        when(event) {
            is RegistrationEvent.OnEmailUpdated -> {
                val email = event.email
                updateState(
                    state.copy(
                        emailAddress = email,
                        isEmailValid = registrationUseCases.validateEmailUseCase(email)
                    )
                )
            }
            is RegistrationEvent.OnNameUpdated -> {
                val name = event.name
                updateState(
                    state.copy(
                        name = name,
                        isNameValid = registrationUseCases.validateNameUseCase(name)
                    )
                )
            }
            is RegistrationEvent.OnPasswordUpdated -> {
                updateState(
                    state.copy(password = event.password)
                )
            }
            RegistrationEvent.SignUp -> validateUserInfo()
        }
    }

    private fun validateUserInfo() {
        if (state.isNameValid) {
            validateEmail()
        } else {
            showError(R.string.name_not_valid)
        }
    }

    private fun validateEmail() {
        if (state.isEmailValid) {
            validatePassword()
        } else {
            showError(R.string.email_not_valid)
        }
    }

    private fun validatePassword() {
        val result = PasswordValidator.validatePassword(
            passwordValidationResult = registrationUseCases.validatePasswordUseCase(state.password)
        )

        if (result.isValid) {
            signUp()
        } else {
            result.error?.let { showError(it) }
        }
    }

    private fun signUp() {
        updateState(state.copy(isLoading = true))
        viewModelScope.launch {
            registrationUseCases.registerUseCase(
                email = state.emailAddress,
                password = state.password,
                fullName = state.name,
            )
                .onSuccess { channel.send(UiRegistrationEvent.OnSignUp) }
                .onFailure {
                    updateState(state.copy(isLoading = false))
                    showError(R.string.network_error_on_login)
                }
        }
    }

    private fun showError(@StringRes message: Int) {
        viewModelScope.launch {
            channel.send(UiRegistrationEvent.ShowErrorMessage(message))
        }
    }

    private fun updateState(newState: RegistrationState) {
        state = newState
        savedStateHandle.set(STATE_KEY, newState)
    }

    companion object {
        private const val STATE_KEY = "state"
    }
}