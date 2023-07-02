package com.vero.tasky.agenda.data.util.multipart

import androidx.core.net.toUri
import com.vero.tasky.agenda.data.util.FileCompressor
import com.vero.tasky.agenda.domain.model.AgendaPhoto
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MultipartParser(
    private val fileCompressor: FileCompressor
) {
    fun getMultipartPhotos(photos: List<AgendaPhoto.LocalPhoto>): List<Pair<File, MultipartBody.Part>> {
        return photos.mapNotNull { photo ->
            photo.uri.toUri().let {
                fileCompressor.getCompressedFile(it)
            }
        }
            .filter { it.length() / FileCompressor.BYTE_IN_KB < FileCompressor.KB_IN_MB }
            .mapIndexed { index, file ->
                Pair(
                    file,
                    MultipartBody.Part
                        .createFormData(
                            "photo$index",
                            file.name,
                            file.asRequestBody()
                        ),
                )
            }
    }
}