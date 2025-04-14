package com.vervyle.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toIntSize
import com.vervyle.model.Plane

// TODO: Does not measure size properly in RowScope
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
    assert(currentAxialIndex < axialSize)
    assert(currentCoronalIndex < coronalSize)
    assert(currentSagittalIndex < sagittalSize)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = plane.name.lowercase())
        Layout(
            modifier.drawWithCache {
                val widthStep = size.width / when (plane) {
                    Plane.AXIAL -> coronalSize
                    Plane.CORONAL -> sagittalSize
                    Plane.SAGITTAL -> axialSize
                }
                val heightStep = size.height / when (plane) {
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
                                when (plane) {
                                    Plane.AXIAL -> currentCoronalIndex
                                    Plane.CORONAL -> currentSagittalIndex
                                    Plane.SAGITTAL -> currentAxialIndex
                                }, 0f
                    )
                    var verticalLinesEnd = Offset(
                        widthStep *
                                when (plane) {
                                    Plane.AXIAL -> currentCoronalIndex
                                    Plane.CORONAL -> currentSagittalIndex
                                    Plane.SAGITTAL -> currentAxialIndex
                                }, size.height
                    )
                    val horizontalLinesStart = Offset(
                        0f, heightStep *
                                when (plane) {
                                    Plane.AXIAL -> currentSagittalIndex
                                    Plane.CORONAL -> currentAxialIndex
                                    Plane.SAGITTAL -> currentCoronalIndex
                                }
                    )
                    val horizontalLinesEnd = Offset(
                        size.width, heightStep *
                                when (plane) {
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
        modifier = Modifier
    )
}
