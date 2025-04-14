package com.vervyle.database.model.old

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PopulatedStructuredBitmap(
    @Embedded
    val mainSliceEntity: SliceEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = BitmapsCrossRef::class,
            parentColumn = "main_bitmap_id",
            entityColumn = "derivative_bitmap_id",
        ),
    )
    val structures: List<SliceEntity>
)
