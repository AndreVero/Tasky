package com.vero.tasky.core.presentation

import com.vero.tasky.R

sealed class ErrorType(val message: Int) {
    object PasswordIsTooShow: ErrorType(R.string.passwrod_is_too_short)
    object PasswordIsNotSecure: ErrorType(R.string.password_is_not_secure)
    object NetworkErrorOnLogin: ErrorType(R.string.network_error_on_login)
}