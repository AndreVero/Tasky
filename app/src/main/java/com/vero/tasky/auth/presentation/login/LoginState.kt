package com.vero.tasky.auth.presentation.login

data class LoginState(
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isEmailValid: Boolean = false,
)
