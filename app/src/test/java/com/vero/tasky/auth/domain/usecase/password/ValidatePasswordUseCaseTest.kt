package com.vero.tasky.auth.domain.usecase.password

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidatePasswordUseCaseTest {

    private val validatePasswordUseCase = ValidatePasswordUseCase()


    @Test
    fun `Validate password, too short password, return error`() {
        val result = validatePasswordUseCase.invoke("")
        assertThat(result).isEqualTo(PasswordValidationResult.TOO_SHORT)
    }

    @Test
    fun `Validate password, no uppercase, return error`() {
        val result = validatePasswordUseCase.invoke("12qwertyu")
        assertThat(result).isEqualTo(PasswordValidationResult.NO_UPPERCASE)
    }

    @Test
    fun `Validate password, no digit, return error`() {
        val result = validatePasswordUseCase.invoke("QWertyuio")
        assertThat(result).isEqualTo(PasswordValidationResult.NO_DIGIT)
    }

    @Test
    fun `Validate password, no lowercase, return error`() {
        val result = validatePasswordUseCase.invoke("12QWERTYUI")
        assertThat(result).isEqualTo(PasswordValidationResult.NO_LOWERCASE)
    }

    @Test
    fun `Validate password, correct password, return success`() {
        val result = validatePasswordUseCase.invoke("wertyuiWE12")
        assertThat(result).isEqualTo(PasswordValidationResult.SUCCESS)
    }
}