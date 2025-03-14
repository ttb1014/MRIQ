package com.vervyle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vervyle.model.Plane

@Entity(
    tableName = "slices"
)
data class SliceEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val plane: Plane,
    @ColumnInfo(name = "file_name")
    val fileName: String,
)
