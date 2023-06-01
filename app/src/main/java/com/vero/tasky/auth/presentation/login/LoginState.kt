package com.vero.tasky.auth.presentation.login

import android.os.Parcelable
import androidx.annotation.StringRes
import com.vero.tasky.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginState(
    val isLoading: Boolean = false,
    val password: String = "",
    val emailAddress: String = "",
    val isEmailValid: Boolean = false,
    val isErrorEmail: Boolean = false,
    val isErrorPassword: Boolean = false,
    @StringRes val emailLabel: Int = R.string.email_hint,
    @StringRes val passwordLabel: Int = R.string.password
) : Parcelable
