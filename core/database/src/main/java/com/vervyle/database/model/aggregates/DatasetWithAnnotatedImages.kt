package com.vervyle.database.model.aggregates

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.vervyle.database.model.DatasetEntity
import com.vervyle.database.model.links.DatasetImageEntity

data class DatasetWithAnnotatedImages(
    @Embedded val dataset: DatasetEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "imageId",
        associateBy = Junction(
            value = DatasetImageEntity::class,
            parentColumn = "datasetId",
            entityColumn = "imageId"
        )
    )
    val annotatedImages: List<MedicalImageWithAnnotations>
)