package com.vervyle.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.modifiers.drawAxis
import com.vervyle.model.Plane

@Composable
fun AnnotatedImagePlaneView(
    plane: Plane,
    bitmap: Bitmap,
    currentAxialIndex: Int,
    currentCoronalIndex: Int,
    currentSagittalIndex: Int,
    axialSize: Int,
    coronalSize: Int,
    sagittalSize: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = plane.name.lowercase() + " ${
                when (plane) {
                    Plane.AXIAL -> currentAxialIndex
                    Plane.CORONAL -> currentCoronalIndex
                    Plane.SAGITTAL -> currentSagittalIndex
                }
            }"
        )

        val xStep = when (plane) {
            Plane.AXIAL -> currentCoronalIndex
            Plane.CORONAL -> currentSagittalIndex
            Plane.SAGITTAL -> currentAxialIndex
        }
        val xMax = when (plane) {
            Plane.AXIAL -> coronalSize
            Plane.CORONAL -> sagittalSize
            Plane.SAGITTAL -> axialSize
        }
        val yStep = when (plane) {
            Plane.AXIAL -> currentSagittalIndex
            Plane.CORONAL -> currentAxialIndex
            Plane.SAGITTAL -> currentCoronalIndex
        }
        val yMax = when (plane) {
            Plane.AXIAL -> sagittalSize
            Plane.CORONAL -> axialSize
            Plane.SAGITTAL -> coronalSize
        }

        Image(
            bitmap.asImageBitmap(),
            null,
            modifier
                .drawAxis(
                    xStep,
                    xMax,
                    yStep,
                    yMax,
                )
        )
    }
}

@Preview
@Composable
private fun AnnotatedImagePlaneViewPreview() {
    val context = LocalContext.current
    val bitmap by remember {
        mutableStateOf(
            BitmapFactory.decodeResource(
                context.resources,
                com.vervyle.mock.R.drawable.axial_0000_0062
            )
        )
    }

    AnnotatedImagePlaneView(
        plane = Plane.SAGITTAL,
        bitmap = bitmap,
        currentAxialIndex = 12,
        currentCoronalIndex = 14,
        currentSagittalIndex = 12,
        axialSize = 156,
        coronalSize = 125,
        sagittalSize = 125,
        modifier = Modifier.size(160.dp)
    )
}
