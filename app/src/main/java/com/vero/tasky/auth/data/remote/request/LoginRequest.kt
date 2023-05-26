package com.vero.tasky.auth.data.remote.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LoginRequest (
    val email: String,
    val password: String
)