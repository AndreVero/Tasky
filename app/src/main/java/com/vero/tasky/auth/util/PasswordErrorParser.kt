package com.vero.tasky.auth.util

import com.vero.tasky.R
import com.vero.tasky.auth.domain.usecase.password.PasswordValidationResult

object PasswordErrorParser {

    fun parse(
        passwordValidationResult: PasswordValidationResult,
    ) : ValidationResult {
        return when (passwordValidationResult) {
            PasswordValidationResult.SUCCESS -> {
                ValidationResult.Valid
            }
            PasswordValidationResult.TOO_SHORT -> {
                ValidationResult.Invalid(error = R.string.password_is_too_short)
            }
            PasswordValidationResult.NO_UPPERCASE -> {
                ValidationResult.Invalid(error = R.string.password_is_not_secure_uppercase)
            }
            PasswordValidationResult.NO_DIGIT -> {
                ValidationResult.Invalid(error = R.string.password_is_too_short)
            }
            PasswordValidationResult.NO_LOWERCASE -> {
                ValidationResult.Invalid(error = R.string.password_is_not_secure_lowercase)
            }
        }
    }

}