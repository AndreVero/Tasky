package com.vero.tasky.agenda.domain.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class UserNameParserTest {

    @Test
    fun `Validate user name, name with one letter, return one symbol uppercase`() {
        val result = UserNameParser.toShortName("q")
        assertThat(result).isEqualTo("Q")
    }

    @Test
    fun `Validate user name, name with zero letters, return empty string`() {
        val result = UserNameParser.toShortName("")
        assertThat(result).isEqualTo("")
    }

    @Test
    fun `Validate user name, name with two words, return first letter of each word uppercase`() {
        val result = UserNameParser.toShortName("qw er")
        assertThat(result).isEqualTo("QE")
    }

    @Test
    fun `Validate user name, name with three words, return first letter of two words uppercase`()
    {
        val result = UserNameParser.toShortName("qw er ty")
        assertThat(result).isEqualTo("QE")
    }

    @Test
    fun `Validate user name, one word, return two first letter uppercase`() {
        val result = UserNameParser.toShortName("qwerty")
        assertThat(result).isEqualTo("QW")
    }

    @Test
    fun `Validate user name, named with symbols, return same symbols`() {
        val result = UserNameParser.toShortName("-1")
        assertThat(result).isEqualTo("-1")
    }
}