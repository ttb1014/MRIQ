package com.vervyle.data.repository

import android.util.Log
import com.vervyle.database.dao.AggregatesDao
import com.vervyle.database.model.AnnotationImageEntity
import com.vervyle.database.model.DatasetEntity
import com.vervyle.database.model.MedicalImageEntity
import com.vervyle.database.model.StructureEntity
import com.vervyle.database.model.aggregates.AnnotationWithStructure
import com.vervyle.database.model.aggregates.DatasetWithAnnotatedImages
import com.vervyle.database.model.aggregates.MedicalImageWithAnnotations
import com.vervyle.model.QuizScreenResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TEST"

internal class OfflineFirstQuizRepository @Inject constructor(
    private val offlineQuizRepository: OfflineQuizRepository,
    private val aggregatesDao: AggregatesDao,
    private val onlineQuizRepository: OnlineQuizRepository
) : QuizRepository {

    override fun getQuizByName(quizName: String): Flow<QuizScreenResource> = flow {
        try {
            emit(offlineQuizRepository.getQuizByName(quizName).first())
        } catch (exc: Exception) {
            exc.printStackTrace()
            Log.d(TAG, exc.message ?: "Error getting dataset from DB!")
            val quizScreenResource = onlineQuizRepository.getQuizByName(quizName).first()
            emit(quizScreenResource)
            val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
            scope.launch {
                try {
                    aggregatesDao.insertDatasetWithAnnotatedImages(quizScreenResource.asLocalModel())
                    Log.d(TAG, "Successfully inserted data into DB")
                } catch (e: Exception) {
                    Log.d(TAG, e.message ?: "Error inserting dataset")
                }
            }
        }
    }

    companion object {
        private const val TAG = "OfflineFirstQuizRepository"
    }
}

fun QuizScreenResource.asLocalModel(): DatasetWithAnnotatedImages {
    return DatasetWithAnnotatedImages(
        dataset = DatasetEntity(
            name = this.quizName
        ),
        annotatedImages = this.annotatedImages.map { (plane, medicalImages) ->
            medicalImages.map { annotatedImage ->
                MedicalImageWithAnnotations(
                    image = MedicalImageEntity(
                        imagePath = "${plane.name.uppercase()}_0000_${annotatedImage.index.toFourDigitString()}.jpg",
                        plane = plane,
                        imageIndex = annotatedImage.index
                    ),
                    annotations = annotatedImage.annotations.map { structureAnnotation ->
                        AnnotationWithStructure(
                            annotation = AnnotationImageEntity(
                                imagePath = "${plane.name.uppercase()}_${annotatedImage.index.toFourDigitString()}_${structureAnnotation.structureId.toFourDigitString()}.png"
                            ),
                            structure = StructureEntity(
                                name = structureAnnotation.structureName,
                                externalId = structureAnnotation.structureId,
                                description = structureAnnotation.structureDescription
                            )
                        )
                    }
                )
            }
        }.flatten()
    )
}

private fun Int.toFourDigitString(): String = this.toString().padStart(4, '0')