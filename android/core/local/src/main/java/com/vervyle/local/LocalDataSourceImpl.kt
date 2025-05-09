package com.vervyle.local

import com.vervyle.database.dao.AggregatesDao
import com.vervyle.database.dao.DatasetDao
import com.vervyle.database.model.AnnotationImageEntity
import com.vervyle.database.model.DatasetEntity
import com.vervyle.database.model.MedicalImageEntity
import com.vervyle.database.model.StructureEntity
import com.vervyle.database.model.aggregates.AnnotationWithStructure
import com.vervyle.database.model.aggregates.DatasetWithAnnotatedImages
import com.vervyle.database.model.aggregates.MedicalImageWithAnnotations
import com.vervyle.disk.DiskManager
import com.vervyle.model.AnnotatedImage
import com.vervyle.model.Plane
import com.vervyle.model.QuizScreenResource
import com.vervyle.model.StructureAnnotation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class LocalDataSourceImpl @Inject constructor(
    private val aggregatesDao: AggregatesDao,
    private val datasetDao: DatasetDao,
    private val diskManager: DiskManager,
) : LocalDataSource {

    override fun getQuizScreenResourceByDatasetName(name: String): Flow<QuizScreenResource> = flow {
        val datasetWithAnnotatedImages = datasetDao.getDatasetWithAnnotatedImagesByName(name)

        datasetWithAnnotatedImages?.let { data ->
            val q = QuizScreenResource(
                quizName = data.dataset.name,
                annotatedImages = Plane.entries.associateWith { plane ->
                    data.annotatedImages
                        .filter { it.image.plane == plane }
                        .map { medicalImageWithAnnotations ->
                            AnnotatedImage(
                                image = diskManager.loadImage(medicalImageWithAnnotations.image.imagePath)
                                    ?: throw RuntimeException("image ${medicalImageWithAnnotations.image.imagePath} not found on disk"),
                                index = medicalImageWithAnnotations.image.imageIndex,
                                annotations = medicalImageWithAnnotations.annotations.map { annotationWithStructure ->
                                    StructureAnnotation(
                                        structureId = annotationWithStructure.structure.id,
                                        baseImageIndex = medicalImageWithAnnotations.image.imageIndex,
                                        structureName = annotationWithStructure.structure.name,
                                        mask = diskManager.loadImage(annotationWithStructure.annotation.imagePath)
                                            ?: throw RuntimeException("image ${medicalImageWithAnnotations.image.imagePath} not found on disk"),
                                        structureDescription = annotationWithStructure.structure.description
                                    )
                                }
                            )
                        }
                }
            )
            emit(q)
        }
    }.flowOn(Dispatchers.IO)

    override fun insertQuizScreenResource(quizScreenResource: QuizScreenResource) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)

        quizScreenResource.annotatedImages.forEach { (plane, annotatedImages) ->
            annotatedImages.forEachIndexed { index, annotatedImage ->
                coroutineScope.launch {
                    diskManager.saveImage(
                        name = "${plane.name.uppercase()}_0000_${index.toFourDigitString()}",
                        bitmap = annotatedImage.image
                    )
                }
                annotatedImage.annotations.forEach { structureAnnotation ->
                    coroutineScope.launch {
                        diskManager.saveImage(
                            name = "${plane.name.uppercase()}_${index.toFourDigitString()}_${structureAnnotation.structureId.toFourDigitString()}",
                            bitmap = structureAnnotation.mask
                        )
                    }
                }
            }
        }

        coroutineScope.launch {
            aggregatesDao.insertDatasetWithAnnotatedImages(
                quizScreenResource.asLocalModel()
            )
        }
    }

    private fun QuizScreenResource.asLocalModel(): DatasetWithAnnotatedImages {
        return DatasetWithAnnotatedImages(
            DatasetEntity(name = quizName),
            run {
                val images = mutableListOf<MedicalImageWithAnnotations>()
                annotatedImages.forEach { (plane, annotatedImages) ->
                    annotatedImages.forEachIndexed { index, annotatedImage ->
                        images.add(
                            MedicalImageWithAnnotations(
                                MedicalImageEntity(
                                    imagePath = "${plane.name.uppercase()}_0000_${index.toFourDigitString()}",
                                    plane = plane,
                                    imageIndex = index
                                ),
                                run {
                                    val annotations = mutableListOf<AnnotationWithStructure>()
                                    annotatedImage.annotations.forEach { structureAnnotation ->
                                        annotations.add(
                                            AnnotationWithStructure(
                                                annotation = AnnotationImageEntity(
                                                    imagePath = "${plane.name.uppercase()}_${index.toFourDigitString()}_${structureAnnotation.structureId.toFourDigitString()}"
                                                ),
                                                structure = StructureEntity(
                                                    name = structureAnnotation.structureName,
                                                    description = structureAnnotation.structureDescription
                                                )
                                            )
                                        )
                                    }
                                    annotations
                                }
                            )
                        )
                    }
                }
                images
            }
        )
    }

    private fun Int.toFourDigitString(): String = this.toString().padStart(4, '0')
}