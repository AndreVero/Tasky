package com.vero.tasky.agenda.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

class FileCompressor(private val context: Context) {

    fun getCompressedFile(file: File): File? {
        return if (file.length() / BYTE_IN_KB >= KB_IN_MB) {
            compressFile(file)
        } else {
            file
        }
    }

    private fun compressFile(file: File): File? {
        val bmp = BitmapFactory.decodeFile(file.absolutePath)
        val resultBmp = bmp.copy(bmp.config, true)
        val resultImageFile = File(context.cacheDir, "${file.name}.jpg")
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