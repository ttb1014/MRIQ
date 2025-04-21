package com.vervyle.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toIntSize
import com.vervyle.design_system.components.IntSlider
import com.vervyle.mock.R
import com.vervyle.model.Plane

@Composable
fun TestComponent(
    bitmap: Bitmap,
    currentAxialIndex: Int,
    currentCoronalIndex: Int,
    currentSagittalIndex: Int,
    axialSize: Int,
    coronalSize: Int,
    sagittalSize: Int,
    currentPlane: Plane,
    modifier: Modifier = Modifier,
) {
    assert(currentAxialIndex < axialSize)
    assert(currentCoronalIndex < coronalSize)
    assert(currentSagittalIndex < sagittalSize)
    Layout(
        modifier.drawWithCache {
            val widthStep = size.width / when (currentPlane) {
                Plane.AXIAL -> coronalSize
                Plane.CORONAL -> sagittalSize
                Plane.SAGITTAL -> axialSize
            }
            val heightStep = size.height / when (currentPlane) {
                Plane.AXIAL -> sagittalSize
                Plane.CORONAL -> axialSize
                Plane.SAGITTAL -> coronalSize
            }
            onDrawWithContent {
                drawImage(
                    bitmap.asImageBitmap(),
                    dstSize = size.toIntSize()
                )

                var verticalLinesStart = Offset(
                    widthStep *
                            when (currentPlane) {
                                Plane.AXIAL -> currentCoronalIndex
                                Plane.CORONAL -> currentSagittalIndex
                                Plane.SAGITTAL -> currentAxialIndex
                            }, 0f
                )
                var verticalLinesEnd = Offset(
                    widthStep *
                            when (currentPlane) {
                                Plane.AXIAL -> currentCoronalIndex
                                Plane.CORONAL -> currentSagittalIndex
                                Plane.SAGITTAL -> currentAxialIndex
                            }, size.height
                )
                val horizontalLinesStart = Offset(
                    0f, heightStep *
                            when (currentPlane) {
                                Plane.AXIAL -> currentSagittalIndex
                                Plane.CORONAL -> currentAxialIndex
                                Plane.SAGITTAL -> currentCoronalIndex
                            }
                )
                val horizontalLinesEnd = Offset(
                    size.width, heightStep *
                            when (currentPlane) {
                                Plane.AXIAL -> currentSagittalIndex
                                Plane.CORONAL -> currentAxialIndex
                                Plane.SAGITTAL -> currentCoronalIndex
                            }
                )
                drawLine(
                    Brush.linearGradient(
                        listOf(Color.Yellow, Color.Blue),
                        horizontalLinesStart,
                        horizontalLinesEnd
                    ),
                    verticalLinesStart,
                    verticalLinesEnd,
                    strokeWidth = 4f,
                )
                drawLine(
                    Brush.linearGradient(
                        listOf(Color.Yellow, Color.Blue),
                        verticalLinesStart,
                        verticalLinesEnd,
                    ),
                    horizontalLinesStart,
                    horizontalLinesEnd,
                    strokeWidth = 4f,
                )
            }
        },
        measurePolicy = { _, constraints ->
            layout(constraints.minWidth, constraints.minHeight) {}
        }
    )
}

@Composable
fun TestWrapper(
    bitmap: Bitmap,
    axialSize: Int,
    coronalSize: Int,
    sagittalSize: Int,
    currentPlane: Plane,
    modifier: Modifier = Modifier
) {
    var currentAxialIndex by remember { mutableIntStateOf(0) }
    var currentCoronalIndex by remember { mutableIntStateOf(0) }
    var currentSagittalIndex by remember { mutableIntStateOf(0) }

    Column(modifier) {
        TestComponent(
            bitmap,
            currentAxialIndex,
            currentCoronalIndex,
            currentSagittalIndex,
            axialSize,
            coronalSize,
            sagittalSize,
            currentPlane,
            Modifier.size(300.dp)
        )
        IntSlider(
            axialSize,
            currentAxialIndex,
            { currentAxialIndex = it },
        )
        IntSlider(
            coronalSize,
            currentCoronalIndex,
            { currentCoronalIndex = it }
        )
        IntSlider(
            coronalSize,
            currentSagittalIndex,
            { currentSagittalIndex = it }
        )
    }
}

@Preview
@Composable
private fun TestComponentPreview() {
    val context = LocalContext.current
    val bitmap by remember {
        mutableStateOf(
            BitmapFactory.decodeResource(context.resources, R.drawable.axial_0000_0062)
        )
    }
    TestWrapper(
        bitmap,
        156,
        125,
        125,
        Plane.SAGITTAL,
        Modifier
            .fillMaxHeight()
            .padding(16.dp)
    )
}