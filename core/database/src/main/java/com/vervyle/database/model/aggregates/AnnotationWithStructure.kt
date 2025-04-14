package com.vervyle.database.model.aggregates

import androidx.room.Embedded
import androidx.room.Relation
import com.vervyle.database.model.links.AnnotationEntity
import com.vervyle.database.model.StructureEntity

data class AnnotationWithStructure(
    @Embedded val annotation: AnnotationEntity,
    @Relation(
        parentColumn = "structureId",
        entityColumn = "id",
        entity = StructureEntity::class
    )
    val structure: StructureEntity
)