package com.vero.tasky.agenda.data.remote

val validGetAttendeeResponseUserExist = """
    {
        "doesUserExist": true,
        "attendee": {
        "email": "test@gmail.com",
        "fullName": "1",
        "userId": "1234"
        }
    }
""".trimIndent()

val validGetAttendeeResponseUserNotExist = """
    {
        "doesUserExist": false,
        "attendee": {
        "email": "test@gmail.com",
        "fullName": "1",
        "userId": "1234"
        }
    }
""".trimIndent()