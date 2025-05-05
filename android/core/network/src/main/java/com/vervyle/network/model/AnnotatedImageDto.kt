package com.vervyle.network.model

data class AnnotatedImageDto(
    val index: Int,
    val pathToImageFile: String,
    val annotations: List<AnnotationDto>
)