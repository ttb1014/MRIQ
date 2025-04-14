package com.vervyle.database.model.old

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class PopulatedMri(
    @Embedded
    val mriEntity: MriEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MriBitmapCrossRef::class,
            parentColumn = "mri_id",
            entityColumn = "bitmaps_cross_ref_id",
        )
    )
    val mriBitmaps: List<PopulatedStructuredBitmap>,
)
