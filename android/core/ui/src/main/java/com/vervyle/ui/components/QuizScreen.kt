package com.vervyle.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vervyle.mock.AnnotatedImagesProvider
import com.vervyle.model.Plane

@Composable
fun QuizScreen(
    axialAnnotatedImages: List<Pair<Bitmap, Map<String, Bitmap>>>,
    coronalAnnotatedImages: List<Pair<Bitmap, Map<String, Bitmap>>>,
    sagittalAnnotatedImages: List<Pair<Bitmap, Map<String, Bitmap>>>,
    currentStructure: String,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    assert(axialAnnotatedImages.isNotEmpty())
    assert(coronalAnnotatedImages.isNotEmpty())
    assert(sagittalAnnotatedImages.isNotEmpty())

    val currentAxialIndex by remember { mutableIntStateOf(0) }
    val currentCoronalIndex by remember { mutableIntStateOf(0) }
    val currentSagittalIndex by remember { mutableIntStateOf(0) }

    val currentPlane by remember { mutableStateOf(Plane.AXIAL) }
    val expandedBitmap = when (currentPlane) {
        Plane.AXIAL -> axialAnnotatedImages[currentAxialIndex].first
        Plane.CORONAL -> coronalAnnotatedImages[currentCoronalIndex].first
        Plane.SAGITTAL -> sagittalAnnotatedImages[currentSagittalIndex].first
    }
    val expandedAnnotation = when (currentPlane) {
        Plane.AXIAL -> axialAnnotatedImages[currentAxialIndex].second[currentStructure]
        Plane.CORONAL -> coronalAnnotatedImages[currentCoronalIndex].second[currentStructure]
        Plane.SAGITTAL -> sagittalAnnotatedImages[currentSagittalIndex].second[currentStructure]
    }

    var guessedStructureName by remember { mutableStateOf("") }

    Column(
        modifier
    ) {
        TopAppBar("Quiz", onBackClick, onSearchClick)
        Spacer(Modifier.height(16.dp))
        AnnotatedImageView(
            expandedBitmap,
            expandedAnnotation,
            Modifier.clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(16.dp))
        UserInput(
            text = guessedStructureName,
            onTextChange = { guessedStructureName = it },
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Plane.entries.forEach { plane ->
                    AnnotatedImagePlaneView(
                        plane = plane,
                        bitmap = when (currentPlane) {
                            Plane.AXIAL -> axialAnnotatedImages[currentAxialIndex].first
                            Plane.CORONAL -> coronalAnnotatedImages[currentCoronalIndex].first
                            Plane.SAGITTAL -> sagittalAnnotatedImages[currentSagittalIndex].first
                        },
                        currentAxialIndex = currentAxialIndex,
                        currentCoronalIndex = currentCoronalIndex,
                        currentSagittalIndex = currentSagittalIndex,
                        axialSize = axialAnnotatedImages.size,
                        coronalSize = coronalAnnotatedImages.size,
                        sagittalSize = sagittalAnnotatedImages.size,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color.Blue, RoundedCornerShape(12.dp))
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Plane.entries.forEach { plane ->
                    PlaneChip(plane)
                    Spacer(Modifier.width(8.dp))
                }
                FrameSlider(
                    when (currentPlane) {
                        Plane.AXIAL -> axialAnnotatedImages.size
                        Plane.CORONAL -> coronalAnnotatedImages.size
                        Plane.SAGITTAL -> sagittalAnnotatedImages.size
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun QuizScreenPreview() {
    val context = LocalContext.current
    val provider = AnnotatedImagesProvider(context)
    QuizScreen(
        axialAnnotatedImages = provider.providesAnnotatedImages(Plane.AXIAL),
        coronalAnnotatedImages = provider.providesAnnotatedImages(Plane.CORONAL),
        sagittalAnnotatedImages = provider.providesAnnotatedImages(Plane.SAGITTAL),
        currentStructure = "0001",
        onBackClick = {},
        onSearchClick = {},
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}
