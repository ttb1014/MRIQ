package com.vervyle.design_system.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.VectorDrawable
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.vervyle.model.Plane

// FIXME: implement state save on decomposition
@Composable
fun QuizView(
    axialStructuredBitmaps: List<Pair<Bitmap, List<Bitmap>>>,
    coronalStructuredBitmaps: List<Pair<Bitmap, List<Bitmap>>>,
    sagittalStructuredBitmaps: List<Pair<Bitmap, List<Bitmap>>>,
    modifier: Modifier = Modifier
) {
    var currentPlane by remember {
        mutableStateOf(Plane.SAGITTAL)
    }
    var currentAxialFrame by remember {
        mutableIntStateOf(0)
    }
    var currentCoronalFrame by remember {
        mutableIntStateOf(0)
    }
    var currentSagittalFrame by remember {
        mutableIntStateOf(104)
    }
    var shownSelections by remember {
        mutableStateOf(emptyList<Int>())
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (currentPlane) {
            Plane.AXIAL -> IntSlider(
                maxValue = axialStructuredBitmaps.size,
                initialValue = currentAxialFrame,
                onValueChange = {
                    currentAxialFrame = it

                }
            )

            Plane.CORONAL -> IntSlider(
                maxValue = coronalStructuredBitmaps.size,
                initialValue = currentCoronalFrame,
                onValueChange = {
                    currentCoronalFrame = it
                }
            )

            Plane.SAGITTAL -> IntSlider(
                maxValue = sagittalStructuredBitmaps.size,
                initialValue = currentSagittalFrame,
                onValueChange = {
                    currentSagittalFrame = it
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                RadioButton(
                    plane = Plane.AXIAL,
                    selected = currentPlane == Plane.AXIAL,
                    onClick = {
                        currentPlane = Plane.AXIAL
                    }
                )
            }
            Box(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                RadioButton(
                    plane = Plane.CORONAL,
                    selected = currentPlane == Plane.CORONAL,
                    onClick = {
                        currentPlane = Plane.CORONAL
                    }
                )
            }
            Box(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                RadioButton(
                    plane = Plane.SAGITTAL,
                    selected = currentPlane == Plane.SAGITTAL,
                    onClick = {
                        currentPlane = Plane.SAGITTAL
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        StructuredImage(
            backgroundImage = when (currentPlane) {
                Plane.AXIAL -> axialStructuredBitmaps[currentAxialFrame].first.asImageBitmap()
                Plane.CORONAL -> coronalStructuredBitmaps[currentCoronalFrame].first.asImageBitmap()
                Plane.SAGITTAL -> sagittalStructuredBitmaps[currentSagittalFrame].first.asImageBitmap()
            },
            structureImages = when (currentPlane) {
                Plane.AXIAL -> axialStructuredBitmaps[currentAxialFrame].second.map { it.asImageBitmap() }
                Plane.CORONAL -> coronalStructuredBitmaps[currentCoronalFrame].second.map { it.asImageBitmap() }
                Plane.SAGITTAL -> sagittalStructuredBitmaps[currentSagittalFrame].second.map { it.asImageBitmap() }
            },
            shownStructuresIndexes = shownSelections,
            onStructureClick = { imageIndices ->
                imageIndices.forEach { imageIndex ->
                    if (shownSelections.contains(imageIndex))
                        shownSelections = shownSelections - imageIndex
                    else
                        shownSelections = shownSelections + imageIndex
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

//@Preview
//@Composable
//private fun QuizViewPreview() {
//    val bitmap =
//        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.im_0001_0060)
//    val structureImages = listOf(
//        LocalContext.current.getDrawable(R.drawable.sel_0001_0001_0060) as VectorDrawable,
//        LocalContext.current.getDrawable(R.drawable.sel_0002_0001_0060) as VectorDrawable,
//        LocalContext.current.getDrawable(R.drawable.sel_0003_0001_0060) as VectorDrawable,
//    )
//    val axialData = listOf(
//        Pair(bitmap,
//            structureImages.map { it.toBitmap() }
//        ),
//        Pair(bitmap,
//            structureImages.map { it.toBitmap() }
//        )
//    )
//    QuizView(
//        axialStructuredBitmaps = axialData,
//        coronalStructuredBitmaps = axialData,
//        sagittalStructuredBitmaps = axialData,
//        modifier = Modifier.fillMaxSize()
//    )
//}