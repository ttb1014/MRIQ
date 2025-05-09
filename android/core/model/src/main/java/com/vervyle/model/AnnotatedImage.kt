package com.vervyle.model

import android.graphics.Bitmap

data class AnnotatedImage(
    val image: Bitmap,
    val index: Int,
    val annotations: List<StructureAnnotation>,
)
