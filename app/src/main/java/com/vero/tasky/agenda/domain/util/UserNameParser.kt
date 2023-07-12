package com.vero.tasky.agenda.domain.util

object UserNameParser {

    fun toShortName(name: String) : String {
        if (name.isEmpty())
            return ""
        val nameParts = name.trim().split(" ")
        if (nameParts.size > 1)
            return "${nameParts[0][0].uppercaseChar()}${nameParts[1][0].uppercaseChar()}"
        else if (nameParts.isNotEmpty() && nameParts[0].length > 1)
            return "${nameParts[0][0].uppercaseChar()}${nameParts[0][1].uppercaseChar()}"
        return name.uppercase()
    }
}