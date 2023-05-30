package com.vero.tasky.auth.util

import com.vero.tasky.R
import com.vero.tasky.auth.domain.usecase.password.PasswordValidationResult

object PasswordErrorParser {

    fun parse(
        passwordValidationResult: PasswordValidationResult,
    ) : PasswordParsedResult {
        return when (passwordValidationResult) {
            PasswordValidationResult.SUCCESS -> {
                PasswordParsedResult.Valid
            }
            PasswordValidationResult.TOO_SHORT -> {
                PasswordParsedResult.Invalid(error = R.string.password_is_too_short)
            }
            PasswordValidationResult.NO_UPPERCASE -> {
                PasswordParsedResult.Invalid(error = R.string.password_is_not_secure_uppercase)
            }
            PasswordValidationResult.NO_DIGIT -> {
                PasswordParsedResult.Invalid(error = R.string.password_is_too_short)
            }
            PasswordValidationResult.NO_LOWERCASE -> {
                PasswordParsedResult.Invalid(error = R.string.password_is_not_secure_lowercase)
            }
        }
    }

}