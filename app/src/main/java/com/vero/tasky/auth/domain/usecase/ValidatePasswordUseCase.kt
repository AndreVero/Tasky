package com.vero.tasky.auth.domain.usecase

import java.util.regex.Pattern

class ValidatePasswordUseCase {

    private val pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)")

    operator fun invoke(password: String): Boolean {
        return password.length > 8 && pattern.matcher(password).matches()
    }

}