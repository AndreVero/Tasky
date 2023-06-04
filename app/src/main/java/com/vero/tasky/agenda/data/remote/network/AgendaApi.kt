package com.vero.tasky.agenda.data.remote.network

import com.vero.tasky.agenda.data.remote.network.dto.AgendaDto
import com.vero.tasky.agenda.data.remote.network.request.SyncAgendaRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AgendaApi {

    @GET("agenda")
    suspend fun getAgendaForDay(
        @Query(value = "timezone") timezone: String,
        @Query(value = "time") time: Long
    ) : AgendaDto

    @POST("syncAgenda")
    suspend fun syncAgenda(
        @Body syncAgendaRequest: SyncAgendaRequest
    )

    @GET("fullAgenda")
    suspend fun getFullAgenda() : AgendaDto
}