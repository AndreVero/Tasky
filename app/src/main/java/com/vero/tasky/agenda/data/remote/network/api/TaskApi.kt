package com.vero.tasky.agenda.data.remote.network.api

import com.vero.tasky.agenda.data.remote.network.dto.TaskDto
import com.vero.tasky.agenda.data.remote.network.request.TaskRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TaskApi {

    @POST("task")
    suspend fun createTask(
        @Body taskRequest: TaskRequest
    ): TaskDto

    @PUT("task")
    suspend fun updateTask(
        @Body taskRequest: TaskRequest
    ): TaskDto

    @GET("task")
    suspend fun fetchTask(
        @Query(value = "taskId") taskId: String
    ): TaskDto

    @DELETE("task")
    suspend fun deleteTask(
        @Query(value = "taskId") taskId: String
    )

}