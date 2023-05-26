package com.vero.tasky.auth.data.remote.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequest (
    val fullName: String,
    val email: String,
    val password: String,
)