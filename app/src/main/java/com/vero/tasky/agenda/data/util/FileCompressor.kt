package com.vero.tasky.agenda.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class FileCompressor(private val context: Context) {

    fun getCompressedFile(uri: Uri): File? {
        val file = compressFile(uri) ?: return null

        return if (file.length() / BYTE_IN_KB <= KB_IN_MB) {
            file
        } else {
            file.delete()
            return null
        }
    }

    private fun compressFile(uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bmp = BitmapFactory.decodeStream(inputStream)
        val resultBmp = bmp.copy(bmp.config, true)
        val resultImageFile = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
        val outputStream = FileOutputStream(resultImageFile)
        val successful = resultBmp.compress(
            Bitmap.CompressFormat.JPEG,
            80,
            outputStream
        )
        return if (successful)
            return resultImageFile
        else
            null
    }

    companion object {
        const val BYTE_IN_KB = 1024
        const val KB_IN_MB = 1024
    }
}