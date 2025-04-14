package com.vervyle.database.model.aggregates

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.vervyle.database.model.links.AnnotationLinkEntity
import com.vervyle.database.model.MedicalImageEntity

data class AnnotatedFrameEntity(
    @Embedded val frame: MedicalImageEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AnnotationLinkEntity::class,
            parentColumn = "frameId",
            entityColumn = "annotationId"
        )
    )
    val annotations: List<MedicalImageEntity>
)