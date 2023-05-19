package com.vero.tasky.auth.data.dto

import com.squareup.moshi.JsonClass
import com.vero.tasky.core.domain.model.User

@JsonClass(generateAdapter = true)
data class UserLoginDto(
    val token: String,
    val userId: String,
    val fullName: String
)

fun UserLoginDto.toUser() : User {
    return User(
        fullName = fullName,
        token = token
    )
}
