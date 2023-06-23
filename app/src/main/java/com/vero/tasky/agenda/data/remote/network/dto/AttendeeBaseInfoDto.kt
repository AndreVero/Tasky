package com.vero.tasky.agenda.data.remote.network.dto

import com.squareup.moshi.JsonClass
import com.vero.tasky.agenda.domain.model.AttendeeBaseInfo

@JsonClass(generateAdapter = true)
data class AttendeeBaseInfoDto(
    val email: String,
    val fullName: String,
    val userId: String,
)

fun AttendeeBaseInfoDto.toAttendeeBaseInfo() : AttendeeBaseInfo {
    return AttendeeBaseInfo(
        email = this.email,
        fullName = this.fullName,
        userId = this.userId
    )
}