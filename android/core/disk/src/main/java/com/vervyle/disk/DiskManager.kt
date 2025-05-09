package com.vervyle.disk

import android.graphics.Bitmap
import java.io.File

interface DiskManager {
    suspend fun saveImage(name: String, bitmap: Bitmap): File

    suspend fun loadImage(name: String): Bitmap?

    fun hasImage(name: String): Boolean

    fun clearCache()
}