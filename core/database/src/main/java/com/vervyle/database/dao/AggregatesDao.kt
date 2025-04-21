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
    suspend fun insertDatasetImageLink(link: DatasetImageLink)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDataset(dataset: DatasetEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertImageAnnotationLink(link: ImageAnnotationLink)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMedicalImage(image: MedicalImageEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAnnotationStructureLink(link: AnnotationStructureLink)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStructure(structure: StructureEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAnnotationImage(image: AnnotationImageEntity)

    @Transaction
    suspend fun insertAnnotationWithStructure(annotationWithStructure: AnnotationWithStructure) {
        insertAnnotationImage(annotationWithStructure.annotation)

        insertStructure(annotationWithStructure.structure)

        insertAnnotationStructureLink(
            AnnotationStructureLink(
                imageId = annotationWithStructure.annotation.id,
                structureId = annotationWithStructure.structure.id
            )
        )
    }

    @Transaction
    suspend fun insertMedicalImageWithAnnotations(imageWithAnnotations: MedicalImageWithAnnotations) {
        insertMedicalImage(imageWithAnnotations.image)

        imageWithAnnotations.annotations.forEach { annotationWithStructure ->
            insertAnnotationWithStructure(annotationWithStructure)
            insertImageAnnotationLink(
                ImageAnnotationLink(
                    imageWithAnnotations.image.id,
                    annotationWithStructure.annotation.id
                )
            )
        }
    }

    @Transaction
    suspend fun insertDatasetWithAnnotatedImages(datasetWithImages: DatasetWithAnnotatedImages) {
        insertDataset(datasetWithImages.dataset)

        datasetWithImages.annotatedImages.forEach { imageWithAnnotations ->
            insertMedicalImageWithAnnotations(imageWithAnnotations)

            insertDatasetImageLink(
                DatasetImageLink(
                    datasetWithImages.dataset.id,
                    imageWithAnnotations.image.id
                )
            )
        }
    }
}