package com.vervyle.database.model.links

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.vervyle.database.model.AnnotationImageEntity
import com.vervyle.database.model.MedicalImageEntity

@Entity(
    tableName = "image_annotations_links",
    primaryKeys = ["frame_id", "annotation_id"],
    foreignKeys = [
        ForeignKey(
            entity = MedicalImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["frame_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AnnotationImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["annotation_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("frame_id"),
        Index("annotation_id")
    ]
)
data class ImageAnnotationLink(
    @ColumnInfo(name = "frame_id")
    val frameId: Int,

    @ColumnInfo(name = "annotation_id")
    val annotationId: Int
)