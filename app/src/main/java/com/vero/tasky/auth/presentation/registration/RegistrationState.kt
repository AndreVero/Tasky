package com.vero.tasky.auth.presentation.registration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegistrationState(
    val isLoading: Boolean = false,
    val name: String = "",
    val isNameValid: Boolean = false,
    val password: String = "",
    val emailAddress: String = "",
    val isEmailValid: Boolean = false,
) : Parcelable
