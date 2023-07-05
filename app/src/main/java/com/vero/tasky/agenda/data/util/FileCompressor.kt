package com.vero.tasky.agenda.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream

class FileCompressor(private val context: Context) {

    fun getCompressedImage(uri: Uri): ByteArray? {
        val byteArray = compressImage(uri) ?: return null

        return if (byteArray.size / BYTE_IN_KB <= KB_IN_MB) {
            byteArray
        } else {
            return null
        }
    }

    private fun compressImage(uri: Uri): ByteArray? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bmp = BitmapFactory.decodeStream(inputStream)

        val resultBmp = bmp.copy(bmp.config, true)
        val outputStream = ByteArrayOutputStream()
        val successful = resultBmp.compress(
            Bitmap.CompressFormat.JPEG,
            80,
            outputStream
        )
        return if (successful)
            return outputStream.toByteArray()
        else
            null
    }

    companion object {
        const val BYTE_IN_KB = 1024
        const val KB_IN_MB = 1024
    }
}