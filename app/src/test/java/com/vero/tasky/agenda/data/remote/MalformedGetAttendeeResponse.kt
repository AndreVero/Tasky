package com.vero.tasky.agenda.data.remote

val malformedGetAttendeeResponse = """
    
        "doesUserExist": false,
        "attendee": {
        "email": "test@gmail.com",
        "fullName": "1",
        "userId": "1234"
        }
    }
""".trimIndent()