package com.vervyle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vervyle.model.Plane

@Entity(
    tableName = "medical_images",
    indices = [
        Index(value = ["image_path"], unique = true),
        Index(value = ["plane"])
    ]
)
data class MedicalImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "image_path")
    val imagePath: String,

    val plane: Plane,

    @ColumnInfo(name = "image_index")
    val imageIndex: Int
)
