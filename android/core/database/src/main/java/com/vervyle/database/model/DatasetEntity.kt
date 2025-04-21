package com.vervyle.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "datasets",
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class DatasetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String
)