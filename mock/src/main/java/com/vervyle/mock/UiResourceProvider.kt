package com.vervyle.mock

import com.vervyle.model.AnnotatedImage
import com.vervyle.model.Plane
import com.vervyle.model.QuizScreenResource
import com.vervyle.model.StructureAnnotation

class UiResourceProvider (
    private val mockAnnotatedImagesProvider: MockAnnotatedImagesProvider
) {
    fun providesQuizScreenResource(): QuizScreenResource {
        return QuizScreenResource(
            quizName = "mock",
            annotatedImages = Plane.entries.associateWith { plane ->
                val planeImages = mockAnnotatedImagesProvider.providesAnnotatedImages(plane)

                planeImages.map { pair ->
                    AnnotatedImage(
                        image = pair.first,
                        annotations = pair.second.entries.map { entry ->
                            StructureAnnotation(
                                "mockAnnotation",
                                entry.value,
                                null
                            )
                        }
                    )
                }
            }
        )
    }
}