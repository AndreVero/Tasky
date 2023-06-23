package com.vero.tasky.agenda.data.remote.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AttendeeCheckDto(
    val doesUserExist: Boolean,
    val attendee: AttendeeBaseInfoDto
)