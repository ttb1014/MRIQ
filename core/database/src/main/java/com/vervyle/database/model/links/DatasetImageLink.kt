package com.vervyle.database.model.links

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.vervyle.database.model.DatasetEntity
import com.vervyle.database.model.MedicalImageEntity

@Entity(
    tableName = "dataset_images",
    primaryKeys = ["dataset_id", "image_id"],
    foreignKeys = [
        ForeignKey(
            entity = DatasetEntity::class,
            parentColumns = ["id"],
            childColumns = ["dataset_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MedicalImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["image_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("dataset_id"),
        Index("image_id")
    ]
)
data class DatasetImageLink(
    @ColumnInfo(name = "dataset_id")
    val datasetId: Int,

    @ColumnInfo(name = "image_id")
    val imageId: Int
)