package com.vero.tasky.agenda.data.util

import java.time.*

object LocalDateTimeConverter {

    fun longToLocalDateTime(timestamp: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    }

    fun localDateTimeToLong(localDateTime: LocalDateTime) : Long {
        return localDateTime.toEpochSecond(ZoneOffset.of(ZoneOffset.systemDefault().id))
    }

    fun localDateToLong(localDateTime: LocalDate) : Long {
        return localDateTime.toEpochDay()
    }

}