package com.vero.tasky.auth.util

import com.vero.tasky.R
import com.vero.tasky.auth.domain.usecase.password.PasswordValidationResult

object PasswordErrorHandler {

    fun onPasswordValid(
        passwordValidationResult: PasswordValidationResult,
        onPasswordIsValid: () -> Unit,
        showError: (message: Int) -> Unit
    ) {
        when (passwordValidationResult) {
            PasswordValidationResult.SUCCESS -> {
                onPasswordIsValid()
            }
            PasswordValidationResult.TOO_SHORT -> {
                showError(R.string.password_is_too_short)
            }
            PasswordValidationResult.NO_UPPERCASE -> {
                showError(R.string.password_is_not_secure_uppercase)
            }
            PasswordValidationResult.NO_DIGIT -> {
                showError(R.string.password_is_not_secure_digit)
            }
            PasswordValidationResult.NO_LOWERCASE -> {
                showError(R.string.password_is_not_secure_lowercase)
            }
        }
    }

}