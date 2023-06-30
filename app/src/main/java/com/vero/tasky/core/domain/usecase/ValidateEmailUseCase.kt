package com.vero.tasky.core.domain.usecase

import com.vero.tasky.core.domain.matcher.EmailMatcher

class ValidateEmailUseCase (
    private val emailMatcher: EmailMatcher
) {
    operator fun invoke(email: String) : Boolean {
        return emailMatcher.matches(email)
    }

}