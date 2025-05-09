package com.vervyle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "structures",
    indices = [
        Index(value = ["name"], unique = true),
        Index(value = ["external_id"], unique = true)
    ]
)

data class StructureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @ColumnInfo(name = "external_id")
    val externalId: Int,
    val description: String?
)
