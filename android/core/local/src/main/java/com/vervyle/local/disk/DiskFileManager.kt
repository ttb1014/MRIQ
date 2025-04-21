package com.vervyle.local.disk

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

internal class DiskFileManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val cacheDir = context.externalCacheDir ?: context.cacheDir
    var compressFormat = DEFAULT_COMPRESS_FORMAT
    var compressQuality = DEFAULT_COMPRESS_QUALITY

    suspend fun saveImage(name: String, bitmap: Bitmap): File {
        val file = File(
            cacheDir, "$name.${compressFormat.name.lowercase()}"
        )
        withContext(Dispatchers.IO) {
            FileOutputStream(file).use {
                bitmap.compress(compressFormat, compressQuality, it)
            }
        }
        return file
    }

    suspend fun loadImage(name: String): Bitmap? {
        val file = File(cacheDir, "$name.${compressFormat.name.lowercase()}")
        return if (file.exists()) {
            withContext(Dispatchers.IO) {
                BitmapFactory.decodeFile(file.absolutePath)
            }
        } else null
    }

    fun hasImage(name: String): Boolean {
        return File(cacheDir, "$name.${compressFormat.name.lowercase()}").exists()
    }

    fun clearCache() {
        cacheDir.listFiles()?.forEach { it.delete() }
    }

    companion object {
        private val DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG
        private const val DEFAULT_COMPRESS_QUALITY = 100
    }
}