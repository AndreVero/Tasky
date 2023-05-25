package com.vero.tasky.auth.domain.usecase.password

import java.util.regex.Pattern

class ValidatePasswordUseCase {

    private val pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+\$")

    operator fun invoke(password: String): PasswordValidationResult {
        if (password.length < 9)
            return PasswordValidationResult.TOO_SHORT
        if (!pattern.matcher(password).matches())
            return PasswordValidationResult.NOT_SECURE
        return PasswordValidationResult.SUCCESS
    }

}