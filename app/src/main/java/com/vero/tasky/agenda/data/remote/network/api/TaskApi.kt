package com.vero.tasky.agenda.data.remote.network.api

import com.vero.tasky.agenda.data.remote.network.request.TaskRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TaskApi {

    @POST("task")
    suspend fun createTask(
        @Body taskRequest: TaskRequest
    )

    @PUT("task")
    suspend fun updateTask(
        @Body taskRequest: TaskRequest
    )

    @DELETE("task")
    suspend fun deleteTask(
        @Query(value = "taskId") taskId: String
    )

}