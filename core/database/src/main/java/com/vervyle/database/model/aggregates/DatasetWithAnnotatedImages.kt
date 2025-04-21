package com.vervyle.database.model.aggregates

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.vervyle.database.model.DatasetEntity
import com.vervyle.database.model.MedicalImageEntity
import com.vervyle.database.model.links.DatasetImageLink

data class DatasetWithAnnotatedImages(
    @Embedded val dataset: DatasetEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        entity = MedicalImageEntity::class,
        associateBy = Junction(
            value = DatasetImageLink::class,
            parentColumn = "dataset_id",
            entityColumn = "image_id"
        )
    )
    val annotatedImages: List<MedicalImageWithAnnotations>
)