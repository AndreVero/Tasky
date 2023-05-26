package com.vero.tasky.auth.domain.usecase.password

enum class PasswordValidationResult {
    SUCCESS,
    TOO_SHORT,
    NO_LOWERCASE,
    NO_UPPERCASE,
    NO_DIGIT,
}