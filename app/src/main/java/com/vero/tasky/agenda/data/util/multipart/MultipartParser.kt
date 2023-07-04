package com.vero.tasky.agenda.data.util.multipart

import androidx.core.net.toUri
import com.vero.tasky.agenda.data.util.FileCompressor
import com.vero.tasky.agenda.domain.model.AgendaPhoto
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID

class MultipartParser(
    private val fileCompressor: FileCompressor
) {
    fun getMultipartPhotos(photos: List<AgendaPhoto.LocalPhoto>): List<MultipartBody.Part> {
        return photos.mapNotNull { photo ->
            photo.uri.toUri().let {
                fileCompressor.getCompressedImage(it)
            }
        }
            .mapIndexed { index, byteArray ->
                MultipartBody.Part
                    .createFormData(
                        "photo$index",
                        UUID.randomUUID().toString(),
                        byteArray.toRequestBody()
                    )
            }
    }
}