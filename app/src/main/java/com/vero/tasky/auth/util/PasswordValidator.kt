package com.vero.tasky.auth.util

import com.vero.tasky.R
import com.vero.tasky.auth.domain.usecase.password.PasswordValidationResult

object PasswordValidator {

    fun validatePassword(
        passwordValidationResult: PasswordValidationResult,
    ) : ValidationResult {
        return when (passwordValidationResult) {
            PasswordValidationResult.SUCCESS -> {
                ValidationResult(isValid = true)
            }
            PasswordValidationResult.TOO_SHORT -> {
                ValidationResult(error = R.string.password_is_too_short)
            }
            PasswordValidationResult.NO_UPPERCASE -> {
                ValidationResult(error = R.string.password_is_not_secure_uppercase)
            }
            PasswordValidationResult.NO_DIGIT -> {
                ValidationResult(error = R.string.password_is_not_secure_digit)
            }
            PasswordValidationResult.NO_LOWERCASE -> {
                ValidationResult(error = R.string.password_is_not_secure_lowercase)
            }
        }
    }

}