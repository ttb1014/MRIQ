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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
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
    modifier: Modifier = Modifier,
) {
    var currentAxialFrame by remember {
        mutableIntStateOf(0)
    }
    var currentCoronalFrame by remember {
        mutableIntStateOf(0)
    }
    var currentSagittalFrame by remember {
        mutableIntStateOf(0)
    }
    var currentPlane by remember {
        mutableStateOf(Plane.AXIAL)
    }

    val axialSize = quizScreenResource.annotatedImages[Plane.AXIAL]?.size!!
    val coronalSize = quizScreenResource.annotatedImages[Plane.CORONAL]?.size!!
    val sagittalSize = quizScreenResource.annotatedImages[Plane.SAGITTAL]?.size!!

    val currentAxialAnnotatedImage =
        quizScreenResource.annotatedImages[Plane.AXIAL]?.get(currentAxialFrame)!!
    val currentCoronalAnnotatedImage =
        quizScreenResource.annotatedImages[Plane.CORONAL]?.get(currentCoronalFrame)!!
    val currentSagittalAnnotatedImage =
        quizScreenResource.annotatedImages[Plane.SAGITTAL]?.get(currentSagittalFrame)!!

    var userInput by remember {
        mutableStateOf("")
    }
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AnnotatedImageView(
            image = when (currentPlane) {
                Plane.AXIAL -> currentAxialAnnotatedImage.image
                Plane.CORONAL -> currentCoronalAnnotatedImage.image
                Plane.SAGITTAL -> currentSagittalAnnotatedImage.image
            },
            annotations = when (currentPlane) {
                Plane.AXIAL -> currentAxialAnnotatedImage.annotations.map { it.mask }
                Plane.CORONAL -> currentCoronalAnnotatedImage.annotations.map { it.mask }
                Plane.SAGITTAL -> currentSagittalAnnotatedImage.annotations.map { it.mask }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp)),
        )
        Spacer(Modifier.height(16.dp))
        UserInput(
            userInput, { userInput = it },
            Modifier.fillMaxWidth()
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
                                Plane.AXIAL -> quizScreenResource.annotatedImages[plane]?.get(
                                    currentAxialFrame
                                )!!.image

                                Plane.CORONAL -> quizScreenResource.annotatedImages[plane]?.get(
                                    currentCoronalFrame
                                )!!.image

                                Plane.SAGITTAL -> quizScreenResource.annotatedImages[plane]?.get(
                                    currentSagittalFrame
                                )!!.image
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
                    PlaneChip(plane, { currentPlane = plane })
                    Spacer(Modifier.width(8.dp))
                }
                FrameSlider(
                    value = when (currentPlane) {
                        Plane.AXIAL -> currentAxialFrame
                        Plane.CORONAL -> currentCoronalFrame
                        Plane.SAGITTAL -> currentSagittalFrame
                    },
                    maxValue = when (currentPlane) {
                        Plane.AXIAL -> axialSize
                        Plane.CORONAL -> coronalSize
                        Plane.SAGITTAL -> sagittalSize
                    },
                    onValueChange = when (currentPlane) {
                        Plane.AXIAL -> { it ->
                            currentAxialFrame = it
                        }

                        Plane.CORONAL -> { it ->
                            currentCoronalFrame = it
                        }

                        Plane.SAGITTAL -> { it ->
                            currentSagittalFrame = it
                        }
                    },
                    modifier = Modifier,
                )
            }
        }
    }
}

@Preview
@Composable
private fun QuizScreenPreview() {
//    QuizScreen(
//        quizScreenResource =
//    )
}