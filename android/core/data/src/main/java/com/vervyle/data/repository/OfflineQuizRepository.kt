package com.vervyle.data.repository

import com.vervyle.database.dao.AggregatesDao
import com.vervyle.database.dao.DatasetDao
import com.vervyle.database.model.aggregates.DatasetWithAnnotatedImages
import com.vervyle.disk.DiskManager
import com.vervyle.model.AnnotatedImage
import com.vervyle.model.Plane
import com.vervyle.model.QuizScreenResource
import com.vervyle.model.StructureAnnotation
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

    private suspend fun DatasetWithAnnotatedImages.asExternalModel(): QuizScreenResource {
        return QuizScreenResource(
            quizName = this.dataset.name,
            annotatedImages = Plane.entries.associateWith { plane ->
                this.annotatedImages
                    .filter { it.image.plane == plane }
                    .map { medicalImageWithAnnotations ->
                        AnnotatedImage(
                            image = diskManager.loadImage(medicalImageWithAnnotations.image.imagePath)!!,
                            index = medicalImageWithAnnotations.image.imageIndex,
                            annotations = medicalImageWithAnnotations.annotations.map { annotationWithStructure ->
                                StructureAnnotation(
                                    structureId = annotationWithStructure.structure.id,
                                    baseImageIndex = medicalImageWithAnnotations.image.imageIndex,
                                    structureName = annotationWithStructure.structure.name,
                                    mask = diskManager.loadImage(annotationWithStructure.annotation.imagePath)!!,
                                    structureDescription = annotationWithStructure.structure.description
                                )
                            }
                        )
                    }
            }
        )
    }
}