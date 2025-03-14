package com.vervyle.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.components.InteractableImage
import com.vervyle.design_system.components.IntSlider
import com.vervyle.design_system.components.RadioButton
import com.vervyle.model.Plane

// FIXME: implement state save on decomposition
@Composable
fun InteractionTool(
    axialStructuredBitmaps: List<Pair<Bitmap, List<Bitmap>>>,
    coronalStructuredBitmaps: List<Pair<Bitmap, List<Bitmap>>>,
    sagittalStructuredBitmaps: List<Pair<Bitmap, List<Bitmap>>>,
    modifier: Modifier = Modifier
) {
    var currentPlane by remember {
        mutableStateOf(Plane.SAGITTAL)
    }
    var currentAxialFrame by remember {
        mutableIntStateOf(61)
    }
    var currentCoronalFrame by remember {
        mutableIntStateOf(0)
    }
    var currentSagittalFrame by remember {
        mutableIntStateOf(104)
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

        InteractableImage(
            backgroundImage = when (currentPlane) {
                Plane.AXIAL -> axialStructuredBitmaps[currentAxialFrame].first.asImageBitmap()
                Plane.CORONAL -> coronalStructuredBitmaps[currentCoronalFrame].first.asImageBitmap()
                Plane.SAGITTAL -> sagittalStructuredBitmaps[currentSagittalFrame].first.asImageBitmap()
            },
            structuresList = when (currentPlane) {
                Plane.AXIAL -> axialStructuredBitmaps[currentAxialFrame].second.map { it.asImageBitmap() }
                Plane.CORONAL -> coronalStructuredBitmaps[currentCoronalFrame].second.map { it.asImageBitmap() }
                Plane.SAGITTAL -> sagittalStructuredBitmaps[currentSagittalFrame].second.map { it.asImageBitmap() }
            },
            modifier = modifier
        )
    }
}