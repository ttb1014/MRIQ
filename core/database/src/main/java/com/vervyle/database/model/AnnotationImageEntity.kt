package com.vervyle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "annotation_images",
    indices = [
        Index(value = ["imagePath"], unique = true),
    ]
)
data class AnnotationImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String,
)
