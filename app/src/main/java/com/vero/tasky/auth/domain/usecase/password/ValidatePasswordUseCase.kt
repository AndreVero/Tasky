package com.vero.tasky.auth.domain.usecase.password

class ValidatePasswordUseCase {

    operator fun invoke(password: String): PasswordValidationResult {
        if (password.length < 9)
            return PasswordValidationResult.TOO_SHORT
        if (!password.any { it.isLowerCase() })
            return PasswordValidationResult.NO_LOWERCASE
        if (!password.any { it.isUpperCase() })
            return PasswordValidationResult.NO_UPPERCASE
        if (!password.any { it.isDigit() })
            return PasswordValidationResult.NO_DIGIT
        return PasswordValidationResult.SUCCESS
    }

}