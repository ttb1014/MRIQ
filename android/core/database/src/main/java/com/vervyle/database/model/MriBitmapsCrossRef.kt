package com.vervyle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "mri_bitmap",
    foreignKeys = [
        ForeignKey(
            entity = MriEntity::class,
            parentColumns = ["id"],
            childColumns = ["mri_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SliceEntity::class,
            parentColumns = ["id"],
            childColumns = ["bitmap_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class MriBitmapCrossRef(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "mri_id")
    val mriId: Int,
    @ColumnInfo(name = "bitmap_id")
    val bitmapId: Int
)