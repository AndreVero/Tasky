package com.vero.tasky.auth.data.remote

val malformedLoginResponse = """
    {
        "token: 123,
        "userId": "ID of the logged in user",
        "fullName": "Full name of the logged in user"
}
""".trimIndent()
