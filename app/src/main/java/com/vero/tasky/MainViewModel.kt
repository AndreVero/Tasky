package com.vero.tasky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.tasky.auth.domain.usecase.AuthenticateUseCase
import com.vero.tasky.core.domain.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: UserPreferences,
    private val authenticateUseCase: AuthenticateUseCase
): ViewModel() {

    private var _state = MutableStateFlow(MainState(
        isLoggedIn = preferences.isLoggedIn()
    ))
    val state = _state.asStateFlow()

    private val channel = Channel<Int>()
    val uiEvent = channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            if (_state.value.isLoggedIn) {
                validateToken()
            } else {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private suspend fun validateToken() {
        authenticateUseCase()
            .onSuccess {
                _state.value = _state.value.copy(isLoading = false)
            }
            .onFailure { error ->
                if (error is HttpException && error.code() == 401) {
                    preferences.clear()
                    _state.value = _state.value.copy(isLoading = false, isLoggedIn = false)
                    channel.send(R.string.token_expired)
                } else {
                    channel.send(R.string.network_error_on_authenticate)
                }
            }
    }
}