package com.vero.tasky.auth.util

import androidx.annotation.StringRes

sealed interface PasswordParsedResult {
    object Valid: PasswordParsedResult
    data class Invalid(@StringRes val error: Int): PasswordParsedResult
}
