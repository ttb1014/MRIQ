package com.vervyle.model.old

data class StructuredMriData(
    val axial: List<StructuredImage>,
    val coronal: List<StructuredImage>,
    val sagittal: List<StructuredImage>,
)
