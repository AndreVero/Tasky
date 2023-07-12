package com.vero.tasky.agenda.data.util

import java.time.*

object LocalDateTimeConverter {

    private const val UTC = "UTC"

    fun longToLocalDateTimeWithTimezone(timestamp: Long): LocalDateTime {
        val utcTime = LocalDateTime
            .ofInstant(
                Instant.ofEpochSecond(timestamp),
                ZoneId.of(UTC)
            )
        return utcTime
            .atZone(ZoneId.of(UTC))
            .withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()))
            .toLocalDateTime()
    }

    fun getEpochForUTC(localDateTime: LocalDateTime) : Long {
        return localDateTime
            .atZone(ZoneId.systemDefault())
            .withZoneSameInstant(ZoneId.of(UTC))
            .toEpochSecond()
    }

    fun getEpochForCurrentTimezone(localDateTime: LocalDateTime) : Long {
        return localDateTime
            .atZone(ZoneId.systemDefault())
            .toEpochSecond()
    }

}