package com.vervyle.network.utils

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Type
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class ZipConverter(private val outputDir: File) : Converter<ResponseBody, List<File>> {

    override fun convert(responseBody: ResponseBody): List<File>? {
        val zipFiles = mutableListOf<File>()
        val buffer = ByteArray(1024)

        try {
            val zipInputStream = ZipInputStream(responseBody.byteStream())

            var zipEntry: ZipEntry? = zipInputStream.nextEntry

            while (zipEntry != null) {
                val file = File(outputDir, zipEntry.name)
                val fos = FileOutputStream(file)

                var len: Int
                while (zipInputStream.read(buffer).also { len = it } > 0) {
                    fos.write(buffer, 0, len)
                }
                fos.close()
                zipFiles.add(file)
                zipEntry = zipInputStream.nextEntry
            }

            zipInputStream.closeEntry()
            zipInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return zipFiles
    }

    class Factory(private val outputDir: File) : Converter.Factory() {
        @OptIn(ExperimentalStdlibApi::class)
        @Suppress("Warnings")
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit,
        ): Converter<ResponseBody, *>? {
            return if (type == typeOf<List<out File>>().javaType) {
                ZipConverter(outputDir)
            } else {
                null
            }
        }
    }
}