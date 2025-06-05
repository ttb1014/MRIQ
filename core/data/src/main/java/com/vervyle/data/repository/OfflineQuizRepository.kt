package com.vervyle.data.repository

import com.vervyle.database.dao.AggregatesDao
import com.vervyle.database.dao.DatasetDao
import com.vervyle.database.model.aggregates.DatasetWithAnnotatedImages
import com.vervyle.disk.DiskManager
import com.vervyle.model.AnnotatedImage
import com.vervyle.model.Plane
import com.vervyle.model.QuizScreenResource
import com.vervyle.model.StructureAnnotation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class OfflineQuizRepository @Inject constructor(
    private val aggregatesDao: AggregatesDao,
    private val datasetDao: DatasetDao,
    private val diskManager: DiskManager,
) : QuizRepository {

    override fun getQuizByName(quizName: String): Flow<QuizScreenResource> = flow {
        val datasetWithAnnotatedImages = datasetDao.getDatasetWithAnnotatedImagesByName(quizName)
        emit(datasetWithAnnotatedImages!!.asExternalModel())
    }

    private suspend fun DatasetWithAnnotatedImages.asExternalModel(): QuizScreenResource = coroutineScope {
        val annotatedImagesByPlane = Plane.entries.associateWith { plane ->
            val imagesForPlane = annotatedImages.filter { it.image.plane == plane }

            imagesForPlane.map { medicalImageWithAnnotations ->
                async(Dispatchers.IO) {
                    val baseImageDeferred = async(Dispatchers.IO) {
                        diskManager.loadImage(medicalImageWithAnnotations.image.imagePath)!!
                    }

                    val annotationsDeferred = medicalImageWithAnnotations.annotations.map { annotationWithStructure ->
                        async(Dispatchers.IO) {
                            val structure = annotationWithStructure.structure
                            val mask = diskManager.loadImage(annotationWithStructure.annotation.imagePath)!!
                            StructureAnnotation(
                                structureId = structure.externalId,
                                baseImageIndex = medicalImageWithAnnotations.image.imageIndex,
                                structureName = structure.name,
                                mask = mask,
                                structureDescription = structure.description
                            )
                        }
                    }

                    AnnotatedImage(
                        image = baseImageDeferred.await(),
                        index = medicalImageWithAnnotations.image.imageIndex,
                        annotations = annotationsDeferred.awaitAll()
                    )
                }
            }.awaitAll()
        }

        QuizScreenResource(
            quizName = dataset.name,
            annotatedImages = annotatedImagesByPlane
        )
    }

}