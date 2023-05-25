package com.vero.tasky.auth.domain.usecase.password

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidatePasswordUseCaseTest {

    private val validatePasswordUseCase = ValidatePasswordUseCase()

    companion object {
        private const val TOO_SHORT_PASSWORD = ""
        private const val PASSWORD_WITHOUT_UPPERCASE = "12qwertyu"
        private const val CORRECT_PASSWORD = "wertyuiWE12"
    }

    @Test fun `Validate password, too short password, return error`() {
        val result = validatePasswordUseCase.invoke(TOO_SHORT_PASSWORD)
        assertThat(result).isEqualTo(PasswordValidationResult.TOO_SHORT)
    }

    @Test fun `Validate password, not secure, return error`() {
        val result = validatePasswordUseCase.invoke(PASSWORD_WITHOUT_UPPERCASE)
        assertThat(result).isEqualTo(PasswordValidationResult.NOT_SECURE)
    }

    @Test fun `Validate password, correct password, return success`() {
        val result = validatePasswordUseCase.invoke(CORRECT_PASSWORD)
        assertThat(result).isEqualTo(PasswordValidationResult.SUCCESS)
    }
}