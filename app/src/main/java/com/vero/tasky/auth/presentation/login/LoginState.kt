package com.vero.tasky.auth.presentation.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginState(
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isEmailValid: Boolean = false,
    val password: String = "",
    val emailAddress: String = ""
) : Parcelable
