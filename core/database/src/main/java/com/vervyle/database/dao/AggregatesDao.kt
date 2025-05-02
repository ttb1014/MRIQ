package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.vervyle.database.model.AnnotationImageEntity
import com.vervyle.database.model.DatasetEntity
import com.vervyle.database.model.MedicalImageEntity
import com.vervyle.database.model.StructureEntity
import com.vervyle.database.model.aggregates.AnnotationWithStructure
import com.vervyle.database.model.aggregates.DatasetWithAnnotatedImages
import com.vervyle.database.model.aggregates.MedicalImageWithAnnotations
import com.vervyle.database.model.links.AnnotationStructureLink
import com.vervyle.database.model.links.DatasetImageLink
import com.vervyle.database.model.links.ImageAnnotationLink

@Dao
interface AggregatesDao {
    // copy-pasted from other DAOs
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDatasetImageLink(link: DatasetImageLink): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDataset(dataset: DatasetEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertImageAnnotationLink(link: ImageAnnotationLink): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMedicalImage(image: MedicalImageEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAnnotationStructureLink(link: AnnotationStructureLink): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStructure(structure: StructureEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAnnotationImage(image: AnnotationImageEntity): Long

    @Transaction
    suspend fun insertAnnotationWithStructure(annotationWithStructure: AnnotationWithStructure): Long {
        val annotationId = insertAnnotationImage(annotationWithStructure.annotation)

        val structureId = insertStructure(annotationWithStructure.structure)

        insertAnnotationStructureLink(
            AnnotationStructureLink(
                imageId = annotationId.toInt(),
                structureId = structureId.toInt()
            )
        )
        return annotationId
    }

    @Transaction
    suspend fun insertMedicalImageWithAnnotations(imageWithAnnotations: MedicalImageWithAnnotations): Long {
        val medicalImageId = insertMedicalImage(imageWithAnnotations.image)

        imageWithAnnotations.annotations.forEach { annotationWithStructure ->
            val annotationId = insertAnnotationWithStructure(annotationWithStructure)
            insertImageAnnotationLink(
                ImageAnnotationLink(
                    medicalImageId.toInt(),
                    annotationId.toInt()
                )
            )
        }
        return medicalImageId
    }

    @Transaction
    suspend fun insertDatasetWithAnnotatedImages(datasetWithImages: DatasetWithAnnotatedImages) {
        val datasetId = insertDataset(datasetWithImages.dataset)

        datasetWithImages.annotatedImages.forEach { imageWithAnnotations ->
            val imageId = insertMedicalImageWithAnnotations(imageWithAnnotations)

            insertDatasetImageLink(
                DatasetImageLink(
                    datasetId.toInt(),
                    imageId.toInt()
                )
            )
        }
    }
}