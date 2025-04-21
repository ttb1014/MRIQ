package com.vervyle.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.vervyle.data.exceptions.BadFileNameException
import com.vervyle.data.repository.old_or_bad.TAG
import com.vervyle.model.AnnotatedImage
import com.vervyle.model.Plane
import com.vervyle.model.QuizScreenResource
import com.vervyle.model.StructureAnnotation
import com.vervyle.network.MriqNetworkDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Locale
import javax.inject.Inject

class OnlineFirstQuizRepository @Inject constructor(
    private val networkDataSource: MriqNetworkDataSource,
    @ApplicationContext context: Context,
) : QuizRepository {

    private data class FileMetadata(
        val plane: Plane,
        val base: String,
        val id: String
    )

    /** TODO добавить получение описания структур
     * @throws BadFileNameException сервер вернул файл с неправильным названием
     * @throws java.net.SocketTimeoutException сервер недоступен
     */
    override fun getQuizByName(datasetName: String): Flow<QuizScreenResource> = flow {
        val files = networkDataSource.getDatasetImagesByName(datasetName)
        val annotations = mutableMapOf<String, MutableList<File>>()

        files.filter { getFileMetadata(it).base != "0000" }
            .forEach {
                annotations.getOrPut(getFileMetadata(it).base) { mutableListOf() }.add(it)
            }

        val annotatedImages = Plane.entries.associateWith { mutableListOf<AnnotatedImage>() }
        files.filter { getFileMetadata(it).base == "0000" }
            .forEach { file ->
                val plane = getFileMetadata(file).plane
                // !! operator must be safe to use
                annotatedImages[plane]!!.add(
                    AnnotatedImage(
                        image = decodeJpg(file),
                        annotations = annotations[getFileMetadata(file).id]
                            ?.filter {
                                getFileMetadata(it).plane == plane
                            }
                            ?.map {
                                StructureAnnotation(
                                    name = it.name,
                                    mask = decodeJpg(it),
                                    description = null
                                )
                            } ?: emptyList()
                    )
                )
            }

        val quizScreenResource = QuizScreenResource(
            datasetName,
            annotatedImages,
        )
        emit(quizScreenResource)
    }

    private fun decodeJpg(file: File): Bitmap {
        var fis: FileInputStream? = null
        try {
            fis = file.inputStream()
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "file not found")
            e.printStackTrace()
        } catch (e: SecurityException) {
            Log.d(TAG, "does not have permission to read")
            e.printStackTrace()
        }
        val bitmap = BitmapFactory.decodeStream(fis)
        fis?.close()
        return bitmap
    }

    private fun getFileMetadata(file: File): FileMetadata {
        val buffer = file.name.split("-", "_", ".")
        if (buffer.size != 4) throw BadFileNameException("Corrupted dataset. Cannot parse filename: ${file.name}")
        return FileMetadata(
            plane = Plane.valueOf(buffer[0].uppercase(Locale.ROOT)),
            base = buffer[1],
            id = buffer[2],
        )
    }
}