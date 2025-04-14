package com.vervyle.database.model.links

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.vervyle.database.model.DatasetEntity
import com.vervyle.database.model.MedicalImageEntity

@Entity(
    tableName = "dataset_images",
    primaryKeys = ["datasetId", "imageId"],
    foreignKeys = [
        ForeignKey(
            entity = DatasetEntity::class,
            parentColumns = ["id"],
            childColumns = ["datasetId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MedicalImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["imageId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["datasetId"]),
        Index(value = ["imageId"])
    ]
)
data class DatasetImageEntity(
    val datasetId: Int,
    val imageId: Int
)