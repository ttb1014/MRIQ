package com.vervyle.quiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.components.FrameSlider
import com.vervyle.design_system.components.PlaneChip
import com.vervyle.design_system.components.UserInput
import com.vervyle.model.Plane
import com.vervyle.model.QuizScreenResource
import com.vervyle.ui.AnnotatedImagePlaneView
import com.vervyle.ui.AnnotatedImageView

@Composable
fun QuizScreen(
    quizScreenResource: QuizScreenResource,
    shownStructures: List<Int>,
    quizzedStructure: Int,
    activePlane: Plane,
    planeToIndexMapping: Map<Plane, Int>,
    onUserInput: (String) -> Unit,
    onPlaneChange: (Plane) -> Unit,
    onPlaneIndexChange: (Plane, Int) -> Unit,
    onAnnotationClick: (Plane, Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val axialSize = quizScreenResource.annotatedImages[Plane.AXIAL]?.size!!
    val coronalSize = quizScreenResource.annotatedImages[Plane.CORONAL]?.size!!
    val sagittalSize = quizScreenResource.annotatedImages[Plane.SAGITTAL]?.size!!

    var userInput by remember {
        mutableStateOf("")
    }

    val axialMedicalImage =
        quizScreenResource.annotatedImages[Plane.AXIAL]!![planeToIndexMapping[Plane.AXIAL]!!]
    val coronalMedicalImage =
        quizScreenResource.annotatedImages[Plane.CORONAL]!![planeToIndexMapping[Plane.CORONAL]!!]
    val sagittalMedicalImage =
        quizScreenResource.annotatedImages[Plane.SAGITTAL]!![planeToIndexMapping[Plane.SAGITTAL]!!]

    val currentAxialFrame = planeToIndexMapping[Plane.AXIAL]!!
    val currentCoronalFrame = planeToIndexMapping[Plane.CORONAL]!!
    val currentSagittalFrame = planeToIndexMapping[Plane.SAGITTAL]!!

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AnnotatedImageView(
            image = when (activePlane) {
                Plane.AXIAL -> axialMedicalImage.image
                Plane.CORONAL -> coronalMedicalImage.image
                Plane.SAGITTAL -> sagittalMedicalImage.image
            },
            annotations = when (activePlane) {
                Plane.AXIAL -> axialMedicalImage.annotations.map { structureAnnotation ->
                    Pair(structureAnnotation.structureId, structureAnnotation.mask)
                }

                Plane.CORONAL -> coronalMedicalImage.annotations.map { structureAnnotation ->
                    Pair(structureAnnotation.structureId, structureAnnotation.mask)
                }

                Plane.SAGITTAL -> sagittalMedicalImage.annotations.map { structureAnnotation ->
                    Pair(structureAnnotation.structureId, structureAnnotation.mask)
                }
            },
            shownAnnotationsIndices = shownStructures,
            quizzedAnnotation = quizzedStructure,
            onAnnotationClick = { annotationIndex ->
                onAnnotationClick(activePlane, planeToIndexMapping[activePlane]!!, annotationIndex)
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp)),
        )
        Spacer(Modifier.height(16.dp))
        UserInput(
            text = userInput,
            onTextChange = { userInput = it },
            onIconPressed = {
                onUserInput(userInput)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val width = (maxWidth - 16.dp) / 3
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Plane.entries.forEach { plane ->
                        AnnotatedImagePlaneView(
                            plane = plane,
                            bitmap = when (plane) {
                                Plane.AXIAL -> quizScreenResource.annotatedImages[plane]!![currentAxialFrame].image

                                Plane.CORONAL -> quizScreenResource.annotatedImages[plane]!![currentCoronalFrame].image

                                Plane.SAGITTAL -> quizScreenResource.annotatedImages[plane]!![currentSagittalFrame].image
                            },
                            currentAxialIndex = currentAxialFrame,
                            currentCoronalIndex = currentCoronalFrame,
                            currentSagittalIndex = currentSagittalFrame,
                            axialSize = axialSize,
                            coronalSize = coronalSize,
                            sagittalSize = sagittalSize,
                            Modifier
                                .size(width)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Plane.entries.forEach { plane ->
                    PlaneChip(
                        plane,
                        onPlaneChange
                    )
                    Spacer(Modifier.width(8.dp))
                }
                FrameSlider(
                    value = when (activePlane) {
                        Plane.AXIAL -> currentAxialFrame
                        Plane.CORONAL -> currentCoronalFrame
                        Plane.SAGITTAL -> currentSagittalFrame
                    },
                    maxValue = when (activePlane) {
                        Plane.AXIAL -> axialSize
                        Plane.CORONAL -> coronalSize
                        Plane.SAGITTAL -> sagittalSize
                    },
                    onValueChange = {
                        onPlaneIndexChange(activePlane, it)
                    },
                    modifier = Modifier,
                )
            }
        }
    }
}