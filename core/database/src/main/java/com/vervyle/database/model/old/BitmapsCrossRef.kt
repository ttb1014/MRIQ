package com.vervyle.database.model.old

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "selections",
    foreignKeys = [
        ForeignKey(
            entity = SliceEntity::class,
            parentColumns = ["id"],
            childColumns = ["main_bitmap_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SliceEntity::class,
            parentColumns = ["id"],
            childColumns = ["derivative_bitmap_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class BitmapsCrossRef(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "main_bitmap_id")
    val mainBitmapId: Int,
    @ColumnInfo(name = "derivative_bitmap_id")
    val derivativeBitmapId: Int
)
