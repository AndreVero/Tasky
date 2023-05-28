package com.vero.tasky.auth.util

import androidx.annotation.StringRes

data class ValidationResult(
    val isValid: Boolean = false,
    @StringRes val error: Int? = null,
)
