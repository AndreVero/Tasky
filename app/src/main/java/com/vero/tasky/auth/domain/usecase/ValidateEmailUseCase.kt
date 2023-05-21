package com.vero.tasky.auth.domain.usecase

import com.vero.tasky.auth.domain.matcher.EmailMatcher
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(
    private val emailMatcher: EmailMatcher
) {

    operator fun invoke(email: String) : Boolean {
        return emailMatcher.matches(email)
    }

}