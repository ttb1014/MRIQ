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
import com.vervyle.network.model.StructureDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

        emit(quizDto.asExternalModel())
    }

    suspend fun QuizDto.asExternalModel(): QuizScreenResource {
        return QuizScreenResource(
            quizName = this.name,
            annotatedImages = Plane.entries.associateWith { plane: Plane ->
                when (plane) {
                    Plane.AXIAL -> this.axialAnnotatedImages.map { it.asExternalModel(this.structures) }
                    Plane.CORONAL -> this.coronalAnnotatedImages.map { it.asExternalModel(this.structures) }
                    Plane.SAGITTAL -> this.sagittalAnnotatedImages.map { it.asExternalModel(this.structures) }
                }
            }
        )
    }

    suspend fun AnnotatedImageDto.asExternalModel(structures: List<StructureDto>): AnnotatedImage =
        coroutineScope {
            val imageDeferred = async(Dispatchers.IO) {
                diskManager.loadImage(this@asExternalModel.pathToImageFile)!!
            }

            val annotationsDeferred = annotations.map { annotationDto ->
                async(Dispatchers.IO) {
                    val structure = structures.first { it.id == annotationDto.structureId }
                    StructureAnnotation(
                        structureId = annotationDto.structureId,
                        baseImageIndex = index,
                        structureName = structure.name,
                        mask = diskManager.loadImage(annotationDto.pathToImageFile)!!,
                        structureDescription = structure.description
                    )
                }
            }

            val annotatedImage = AnnotatedImage(
                image = imageDeferred.await(),
                index = this@asExternalModel.index,
                annotations = annotationsDeferred.awaitAll()
            )

            return@coroutineScope annotatedImage
        }
}