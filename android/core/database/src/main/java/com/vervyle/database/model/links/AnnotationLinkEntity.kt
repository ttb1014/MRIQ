package com.vervyle.database.model.links

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.vervyle.database.model.AnnotationImageEntity
import com.vervyle.database.model.MedicalImageEntity

@Entity(
    tableName = "annotation_links",
    primaryKeys = ["frameId", "annotationId"],
    foreignKeys = [
        ForeignKey(
            entity = MedicalImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["frameId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AnnotationImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["annotationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("frameId"),
        Index("annotationId")
    ]
)
data class AnnotationLinkEntity(
    val frameId: Int,
    val annotationId: Int
)