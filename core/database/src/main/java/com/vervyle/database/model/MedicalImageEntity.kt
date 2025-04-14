package com.vervyle.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "medical_images",
    indices = [
        Index(value = ["imagePath"], unique = true),
        Index(value = ["plane"])
    ]
)
data class MedicalImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String,
    val plane: Plane,
    val index: Int
)
