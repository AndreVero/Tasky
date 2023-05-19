package com.vero.tasky.auth.data.remote.request

data class RegisterRequest (
    val fullName: String,
    val email: String,
    val password: String,
)