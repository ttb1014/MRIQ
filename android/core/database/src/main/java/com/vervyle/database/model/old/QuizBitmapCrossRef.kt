package com.vervyle.database.model.old

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "quiz_bitmaps",
    primaryKeys = [
        "mri_id"
    ],
    foreignKeys = [
        ForeignKey(
            entity = MriEntity::class,
            parentColumns = ["id"],
            childColumns = ["mri_id"]
        ),
        ForeignKey(
            entity = SliceEntity::class,
            parentColumns = ["id"],
            childColumns = ["header_bitmap_id"]
        )
    ]
)
data class QuizBitmapCrossRef(
    @ColumnInfo("mri_id")
    val mriId: Int,
    @ColumnInfo("header_bitmap_id")
    val headerBitmapId: Int?,
)
