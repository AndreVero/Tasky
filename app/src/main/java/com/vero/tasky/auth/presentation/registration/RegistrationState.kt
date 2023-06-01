package com.vero.tasky.auth.presentation.registration

import android.os.Parcelable
import androidx.annotation.StringRes
import com.vero.tasky.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegistrationState(
    val isLoading: Boolean = false,
    val name: String = "",
    val isNameValid: Boolean = false,
    val password: String = "",
    val emailAddress: String = "",
    val isEmailValid: Boolean = false,
    val isErrorEmail: Boolean = false,
    val isErrorName: Boolean = false,
    val isErrorPassword: Boolean = false,
    @StringRes val emailLabel: Int = R.string.email_hint,
    @StringRes val passwordLabel: Int = R.string.password,
    @StringRes val nameLabel: Int = R.string.name_hint
) : Parcelable
