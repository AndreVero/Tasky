package com.vero.tasky.agenda.data.remote.network.api

import com.vero.tasky.agenda.data.remote.network.dto.AttendeeCheckDto
import com.vero.tasky.agenda.data.remote.network.dto.EventDto
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface EventApi {

    @Multipart
    @POST("event")
    suspend fun createEvent(
        @Part id: MultipartBody.Part,
        @Part title: MultipartBody.Part,
        @Part description: MultipartBody.Part,
        @Part from: MultipartBody.Part,
        @Part to: MultipartBody.Part,
        @Part remindAt: MultipartBody.Part,
        @Part attendeeIds: List<MultipartBody.Part>,
        @Part photos: List<MultipartBody.Part>
    ) : EventDto

    @Multipart
    @PUT("event")
    suspend fun updateEvent(
        @Part id: MultipartBody.Part,
        @Part title: MultipartBody.Part,
        @Part description: MultipartBody.Part,
        @Part from: MultipartBody.Part,
        @Part to: MultipartBody.Part,
        @Part remindAt: MultipartBody.Part,
        @Part isGoing: MultipartBody.Part,
        @Part attendeeIds: List<MultipartBody.Part>,
        @Part deletedPhotoKeys: List<MultipartBody.Part>,
        @Part photos: List<MultipartBody.Part>
    ) : EventDto

    @GET("event")
    suspend fun fetchEvent(@Query("eventId") eventId: String) : EventDto

    @DELETE("event")
    suspend fun deleteEvent(@Query("eventId") eventId: String)

    @DELETE("attendee")
    suspend fun deleteAttendee(@Query("eventId") eventId: String)

    @GET("attendee")
    suspend fun getAttendee(@Query("email") email: String) : AttendeeCheckDto
}