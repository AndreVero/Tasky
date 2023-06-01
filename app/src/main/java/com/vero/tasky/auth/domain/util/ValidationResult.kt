package com.vero.tasky.auth.domain.util

import androidx.annotation.StringRes

sealed interface ValidationResult {
    object Valid: ValidationResult
    data class Invalid(@StringRes val error: Int): ValidationResult
}
