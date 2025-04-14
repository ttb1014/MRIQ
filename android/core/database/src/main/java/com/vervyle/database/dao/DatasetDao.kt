package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vervyle.database.model.DatasetEntity
import com.vervyle.database.model.MedicalImageEntity
import com.vervyle.database.model.StructureEntity
import com.vervyle.database.model.aggregates.AnnotationWithStructure
import com.vervyle.database.model.aggregates.DatasetWithAnnotatedImages
import com.vervyle.database.model.aggregates.MedicalImageWithAnnotations
import com.vervyle.database.model.links.AnnotationEntity
import com.vervyle.database.model.links.DatasetImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DatasetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataset(dataset: DatasetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDatasetImage(datasetImage: DatasetImageEntity)

    @Transaction
    suspend fun insertDatasetWithAnnotatedImages(datasetWithAnnotatedImages: DatasetWithAnnotatedImages) {
        val datasetId = insertDataset(datasetWithAnnotatedImages.dataset)

        datasetWithAnnotatedImages.annotatedImages.forEach { annotatedImage ->
            insertMedicalImageWithAnnotations(annotatedImage)
        }
    }

    @Transaction
    suspend fun insertMedicalImageWithAnnotations(medicalImageWithAnnotations: MedicalImageWithAnnotations) {
        val imageId = insertMedicalImage(medicalImageWithAnnotations.image)

        medicalImageWithAnnotations.annotations.forEach { annotation ->
            insertAnnotationWithStructure(annotation)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnotation(annotation: AnnotationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStructure(structure: StructureEntity): Long

    @Transaction
    suspend fun insertAnnotationWithStructure(annotation: AnnotationWithStructure) {
        val structureId = insertStructure(annotation.structure)

        val updatedAnnotation = annotation.annotation.copy(structureId = structureId.toInt())
        insertAnnotation(updatedAnnotation)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalImage(image: MedicalImageEntity): Long

    @Query("SELECT * FROM datasets")
    fun getAllDatasets(): Flow<List<DatasetEntity>>
}