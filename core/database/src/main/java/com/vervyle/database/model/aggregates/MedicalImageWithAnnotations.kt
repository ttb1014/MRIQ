package com.vervyle.database.model.aggregates

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.vervyle.database.model.AnnotationImageEntity
import com.vervyle.database.model.MedicalImageEntity
import com.vervyle.database.model.links.ImageAnnotationLink

data class MedicalImageWithAnnotations(
    @Embedded val image: MedicalImageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        entity = AnnotationImageEntity::class,
        associateBy = Junction(
            value = ImageAnnotationLink::class,
            parentColumn = "frame_id",
            entityColumn = "annotation_id"
        )
    )
    val annotations: List<AnnotationWithStructure>
)