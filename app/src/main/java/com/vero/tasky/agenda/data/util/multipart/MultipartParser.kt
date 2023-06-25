package com.vero.tasky.agenda.data.util.multipart

import com.vero.tasky.agenda.data.util.FileCompressor
import com.vero.tasky.agenda.domain.model.AgendaPhoto
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MultipartParser(
    private val fileCompressor: FileCompressor
) {
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