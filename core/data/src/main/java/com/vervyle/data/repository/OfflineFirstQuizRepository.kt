package com.vervyle.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.vervyle.data.exceptions.BadFileNameException
import com.vervyle.local.LocalDataSource
import com.vervyle.model.AnnotatedImage
import com.vervyle.model.Plane
import com.vervyle.model.QuizScreenResource
import com.vervyle.model.StructureAnnotation
import com.vervyle.network.MriqNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Locale
import javax.inject.Inject

private const val TAG = "TEST"

internal class OfflineFirstQuizRepository @Inject constructor(
    private val networkDataSource: MriqNetworkDataSource,
    private val localDataSource: LocalDataSource,
) : QuizRepository {

    private data class FileMetadata(
        val plane: Plane,
        val base: String,
        val id: String
    )

    override fun getQuizByName(datasetName: String): Flow<QuizScreenResource> = flow {
        try {
            val flow = localDataSource.getQuizScreenResourceByDatasetName(datasetName)
            this.emit(flow.first())
        } catch (e: NoSuchElementException) {
            Log.d(TAG, "getQuizByName: local datasource is empty, collecting from network")
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
            localDataSource.insertQuizScreenResource(quizScreenResource)
            emit(quizScreenResource)
        }
    }

    private fun decodeJpg(file: File): Bitmap {
        var fis: FileInputStream? = null
        try {
            fis = file.inputStream()
        } catch (e: FileNotFoundException) {
            Log.d(com.vervyle.data.repository.old_or_bad.TAG, "file not found")
            e.printStackTrace()
        } catch (e: SecurityException) {
            Log.d(com.vervyle.data.repository.old_or_bad.TAG, "does not have permission to read")
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