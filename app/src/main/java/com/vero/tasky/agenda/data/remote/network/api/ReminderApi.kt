package com.vero.tasky.agenda.data.remote.network.api

import com.vero.tasky.agenda.data.remote.network.request.ReminderRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ReminderApi {

    @POST("reminder")
    suspend fun createReminder(
        @Body reminderRequest: ReminderRequest
    )

    @PUT("task")
    suspend fun updateReminder(
        @Body reminderRequest: ReminderRequest
    )

    @DELETE("task")
    suspend fun deleteReminder(
        @Query(value = "reminderId") reminderId: String
    )

}