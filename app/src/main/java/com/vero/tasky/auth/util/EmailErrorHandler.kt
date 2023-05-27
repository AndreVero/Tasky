package com.vero.tasky.auth.util

import com.vero.tasky.R

object EmailErrorHandler {

    fun validateEmail(
        isEmailValid: Boolean,
        onEmailIsValid: () -> Unit,
        showError: (message: Int) -> Unit
    ) {
        if (isEmailValid) {
            onEmailIsValid()
        } else {
            showError(R.string.email_not_valid)
        }
    }

}