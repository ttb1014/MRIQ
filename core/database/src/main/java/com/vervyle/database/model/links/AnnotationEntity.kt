package com.vervyle.database.model.links

import androidx.room.Entity
import androidx.room.ForeignKey
import com.vervyle.database.model.AnnotationImageEntity
import com.vervyle.database.model.StructureEntity

@Entity(
    tableName = "annotations",
    primaryKeys = ["imageId", "structureId"],
    foreignKeys = [
        ForeignKey(
            entity = AnnotationImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["imageId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = StructureEntity::class,
            parentColumns = ["id"],
            childColumns = ["structureId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AnnotationEntity(
    val imageId: Int,
    val structureId: Int,
)