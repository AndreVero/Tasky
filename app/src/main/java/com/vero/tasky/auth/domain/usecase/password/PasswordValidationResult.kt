package com.vero.tasky.auth.domain.usecase.password

enum class PasswordValidationResult {
    SUCCESS,
    TOO_SHORT,
    NOT_SECURE
}