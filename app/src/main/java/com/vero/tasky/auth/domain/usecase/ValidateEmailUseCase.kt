package com.vero.tasky.auth.domain.usecase

import com.vero.tasky.auth.domain.matcher.EmailMatcher

class ValidateEmailUseCase (
    private val emailMatcher: EmailMatcher
) {
    operator fun invoke(email: String) : Boolean {
        return emailMatcher.matches(email)
    }

}