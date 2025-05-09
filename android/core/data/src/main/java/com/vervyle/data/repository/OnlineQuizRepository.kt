package com.vervyle.data.repository

import com.vervyle.data.exceptions.BadFileNameException
import com.vervyle.disk.DiskManager
import com.vervyle.model.AnnotatedImage
import com.vervyle.model.Plane
import com.vervyle.model.QuizScreenResource
import com.vervyle.model.StructureAnnotation
import com.vervyle.network.MriqNetworkDataSource
import com.vervyle.network.model.AnnotatedImageDto
import com.vervyle.network.model.QuizDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OnlineQuizRepository @Inject constructor(
    private val networkDataSource: MriqNetworkDataSource,
    private val diskManager: DiskManager,
) : QuizRepository {

    /**
     * @throws BadFileNameException сервер вернул файл с неправильным названием
     * @throws java.net.SocketTimeoutException сервер недоступен
     * @throws NullPointerException
     */
    override fun getQuizByName(quizName: String): Flow<QuizScreenResource> = flow {
        val quizDto = networkDataSource.getQuizById(quizName)

        val annotatedImages = Plane.entries.associateWith { plane ->
            when (plane) {
                Plane.AXIAL -> quizDto.axialAnnotatedImages.map { annotatedImageDto ->
                    withContext(Dispatchers.IO) {
                        annotatedImageDto.asExternalModel(quizDto)
                    }
                }

                Plane.CORONAL -> quizDto.coronalAnnotatedImages.map { annotatedImageDto ->
                    withContext(Dispatchers.IO) {
                        annotatedImageDto.asExternalModel(quizDto)
                    }
                }

                Plane.SAGITTAL -> quizDto.coronalAnnotatedImages.map { annotatedImageDto ->
                    withContext(Dispatchers.IO) {
                        annotatedImageDto.asExternalModel(quizDto)
                    }
                }
            }
        }

        val quizScreenResource = QuizScreenResource(
            quizDto.name,
            annotatedImages,
        )
        emit(quizScreenResource)
    }

    private suspend fun AnnotatedImageDto.asExternalModel(quizDto: QuizDto): AnnotatedImage {
        return AnnotatedImage(
            image = diskManager.loadImage(this.pathToImageFile)!!,
            index = this.index,
            annotations = this.annotations.map { annotationDto ->
                StructureAnnotation(
                    structureId = annotationDto.structureId,
                    baseImageIndex = this.index,
                    structureName = quizDto.structures.first { it.id == annotationDto.structureId }.name,
                    mask = diskManager.loadImage(annotationDto.pathToImageFile)!!,
                    structureDescription = quizDto.structures.first { it.id == annotationDto.structureId }.description
                )
            }
        )
    }
}