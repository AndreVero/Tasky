package com.vero.tasky.agenda.data.util

import java.time.*

object LocalDateTimeConverter {

    fun longToLocalDateTime(timestamp: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())
    }

    fun localDateTimeToLong(localDateTime: LocalDateTime) : Long {
        return localDateTime.toEpochSecond(ZoneId.of(ZoneId.systemDefault().id).rules.getOffset(Instant.now()))
    }

}