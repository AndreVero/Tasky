package com.vero.tasky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.tasky.core.domain.local.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: Preferences
): ViewModel() {

    private var _state = MutableStateFlow(MainState(
        isLoggedIn = preferences.isLoggedIn()
    ))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            if (!_state.value.isLoggedIn) {
                // imitates /authenticate network request
                delay(2000)
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}