package com.vero.tasky.agenda.data.util.multipart

import com.vero.tasky.agenda.data.util.FileCompressor
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.model.AgendaPhoto
import com.vero.tasky.agenda.domain.model.Attendee
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.LocalDateTime

class MultipartParser(
    private val fileCompressor: FileCompressor
) {

    fun getMultipartId(id: String)
        = MultipartBody.Part.createFormData("id", id)

    fun getMultipartTitle(title: String)
            = MultipartBody.Part.createFormData("title", title)

    fun getMultipartDescription(description: String?)
            = MultipartBody.Part.createFormData("description", description ?: "")

    fun getMultipartIsGoing(isGoing: Boolean)
            = MultipartBody.Part.createFormData("isGoing", isGoing.toString())

    fun getMultipartFrom(from: LocalDateTime) =
        MultipartBody.Part.createFormData(
            "from",
            LocalDateTimeConverter.localDateTimeToLong(from).toString())

    fun getMultipartTo(to: LocalDateTime) =
        MultipartBody.Part.createFormData(
            "to",
            LocalDateTimeConverter.localDateTimeToLong(to).toString())

    fun getMultipartRemindAt(remindAt: LocalDateTime) =
        MultipartBody.Part.createFormData(
            "remindAt",
            LocalDateTimeConverter.localDateTimeToLong(remindAt).toString()
        )

    fun getMultipartAttendeesIds(attendees: List<Attendee>) =
        attendees.map { attendee ->
            MultipartBody.Part.createFormData(
                "attendees[]",
                attendee.userId
            )
        }

    fun getMultipartDeletedPhotosKeys(photos: List<String>) =
        photos.map { photo ->
            MultipartBody.Part.createFormData(
                "deletedPhotoKeys[]",
                photo
            )
        }

    fun getMultipartPhotos(photos: List<AgendaPhoto.LocalPhoto>): List<MultipartBody.Part> {
        return photos.mapNotNull { photo ->
            fileCompressor.getCompressedFile(File(photo.uri))
        }
            .filter { it.length() / FileCompressor.BYTE_IN_KB < FileCompressor.KB_IN_MB }
            .map {
                MultipartBody.Part
                    .createFormData(
                        "photos[]",
                        it.name,
                        it.asRequestBody()
                    )
            }
    }
}