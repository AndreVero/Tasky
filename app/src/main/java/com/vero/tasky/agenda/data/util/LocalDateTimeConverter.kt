package com.vero.tasky.agenda.data.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

object LocalDateTimeConverter {

    fun longToLocalDateTime(timestamp: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    }

    fun localDateTimeToLong(localDateTime: LocalDateTime) : Long {
        return localDateTime.toEpochSecond(ZoneOffset.UTC)
    }

}