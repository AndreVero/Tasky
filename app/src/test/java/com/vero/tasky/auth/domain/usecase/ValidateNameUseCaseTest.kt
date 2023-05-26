package com.vero.tasky.auth.domain.usecase

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidateNameUseCaseTest {

    private val validateNameUseCase = ValidateNameUseCase()

    @Test fun `Validate name, too short name, result false`() {
        val result = validateNameUseCase.invoke("")
        assertThat(result).isFalse()
    }

    @Test fun `Validate name, too long name, result false`() {
        val longStr = StringBuilder()
        for (i in 0..51)
            longStr.append(i)
        val result = validateNameUseCase.invoke(longStr.toString())
        assertThat(result).isFalse()
    }

    @Test fun `Validate name, correct name, result true`() {
        val result = validateNameUseCase.invoke("Test")
        assertThat(result).isTrue()
    }
}