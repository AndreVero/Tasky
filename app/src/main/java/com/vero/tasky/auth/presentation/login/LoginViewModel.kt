package com.vero.tasky.auth.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.vero.tasky.auth.domain.usecase.LoginUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val loginUseCases: LoginUseCases) : ViewModel() {


}