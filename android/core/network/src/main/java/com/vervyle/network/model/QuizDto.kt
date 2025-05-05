package com.vervyle.network.model

data class QuizDto(
    val id: String,
    val structures: List<StructureDto>,
    val axialAnnotatedImages: List<AnnotatedImageDto>,
    val coronalAnnotatedImages: List<AnnotatedImageDto>,
    val sagittalAnnotatedImages: List<AnnotatedImageDto>,
    val name: String? = id,
)
