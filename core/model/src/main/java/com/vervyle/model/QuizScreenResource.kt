package com.vervyle.model

data class QuizScreenResource(
    val quizName: String,
    val annotatedImages: Map<Plane, List<AnnotatedImage>>
)
