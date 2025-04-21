package com.vervyle.database.model.links

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.vervyle.database.model.AnnotationImageEntity
import com.vervyle.database.model.StructureEntity

@Entity(
    tableName = "annotations",
    primaryKeys = ["image_id", "structure_id"],
    indices = [
        Index("image_id"),
        Index("structure_id"),
    ],
    foreignKeys = [
        ForeignKey(
            entity = AnnotationImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["image_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = StructureEntity::class,
            parentColumns = ["id"],
            childColumns = ["structure_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AnnotationStructureLink(
    @ColumnInfo(name = "image_id")
    val imageId: Int,

    @ColumnInfo(name = "structure_id")
    val structureId: Int,
)