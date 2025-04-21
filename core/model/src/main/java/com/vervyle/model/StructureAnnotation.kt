package com.vervyle.model

import android.graphics.Bitmap

data class StructureAnnotation(
    val name: String,
    val mask: Bitmap,
    val description: String?,
)
