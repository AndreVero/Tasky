package com.vero.tasky.agenda.presentation.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class LocalDateParserTest {

    @Test
    fun `Get next days for day, result success`() {
        val result = LocalDateParser.getNextDaysFromDate(
            LocalDate.of(
                2023, 1, 4
            )
        )
        assertThat(result[0].date).isEqualTo(
            LocalDate.of(
                2023, 1, 1
            )
        )
        assertThat(result[1].date).isEqualTo(
            LocalDate.of(
                2023, 1, 2
            )
        )
        assertThat(result[2].date).isEqualTo(
            LocalDate.of(
                2023, 1, 3
            )
        )
        assertThat(result[3].date).isEqualTo(
            LocalDate.of(
                2023, 1, 4
            )
        )
        assertThat(result[4].date).isEqualTo(
            LocalDate.of(
                2023, 1, 5
            )
        )
        assertThat(result[5].date).isEqualTo(
            LocalDate.of(
                2023, 1, 6
            )
        )
        assertThat(result[6].date).isEqualTo(
            LocalDate.of(
                2023, 1, 7
            )
        )
    }

    @Test
    fun `Get day label, is valid result`() {
        val result = LocalDateParser.getDayLabel(LocalDate.of(
            2023, 1, 1
        ), Locale.ENGLISH)
        assertThat(result).isEqualTo("01 January 2023")
    }

    @Test
    fun `Get time label, is valid result`() {
        val result = LocalDateParser.getTimeLabel(
            LocalDateTime.of(
            2023, 1, 1, 1, 1
        ))
        assertThat(result).isEqualTo("01:01")
    }

    @Test
    fun `Get short day label, is valid result`() {
        val result = LocalDateParser.getShortDayLabel(
            LocalDateTime.of(
                2023, 1, 1, 1, 1
            ), Locale.ENGLISH)
        assertThat(result).isEqualTo("Jan 01 2023")
    }
}