package com.vervyle.local

import com.vervyle.database.dao.AggregatesDao
import com.vervyle.database.dao.DatasetDao
import com.vervyle.local.disk.DiskFileManager
import com.vervyle.model.AnnotatedImage
import com.vervyle.model.Plane
import com.vervyle.model.QuizScreenResource
import com.vervyle.model.StructureAnnotation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class LocalDataSourceImpl @Inject constructor(
    private val aggregatesDao: AggregatesDao,
    private val datasetDao: DatasetDao,
    private val diskFileManager: DiskFileManager,
) : LocalDataSource {

    override fun getQuizScreenResourceByDatasetName(name: String): Flow<QuizScreenResource> = flow {
        val dataset = datasetDao.getDatasetWithAnnotatedImagesByName(name)

        dataset?.let { data ->
            val q = QuizScreenResource(
                quizName = data.dataset.name,
                annotatedImages = Plane.entries.associateWith { plane ->
                    data.annotatedImages
                        .filter { it.image.plane == plane }
                        .map { annotatedImage ->
                            AnnotatedImage(
                                image = diskFileManager.loadImage(annotatedImage.image.imagePath)
                                    ?: throw CancellationException("image ${annotatedImage.image.imagePath} not found on disk"),
                                annotations = annotatedImage.annotations.map { structureAnnotation ->
                                    StructureAnnotation(
                                        name = structureAnnotation.structure.name,
                                        mask = diskFileManager.loadImage(structureAnnotation.annotation.imagePath)
                                            ?: throw CancellationException("image ${annotatedImage.image.imagePath} not found on disk"),
                                        description = structureAnnotation.structure.description
                                    )
                                }
                            )
                        }
                }
            )
            emit(q)
        }
    }.flowOn(Dispatchers.IO)
}