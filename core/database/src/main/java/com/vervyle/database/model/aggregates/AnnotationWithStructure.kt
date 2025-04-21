package com.vervyle.database.model.aggregates

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.vervyle.database.model.AnnotationImageEntity
import com.vervyle.database.model.StructureEntity
import com.vervyle.database.model.links.AnnotationStructureLink

data class AnnotationWithStructure(
    @Embedded val annotation: AnnotationImageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AnnotationStructureLink::class,
            parentColumn = "image_id",
            entityColumn = "structure_id"
        )
    )
    val structure: StructureEntity
)