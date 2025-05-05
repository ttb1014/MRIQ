package com.vervyle.model

import android.graphics.Bitmap

data class StructureAnnotation(
    val structureId: Int,
    val baseImageIndex: Int,
    val structureName: String,
    val mask: Bitmap,
    val structureDescription: String? = null,
)
