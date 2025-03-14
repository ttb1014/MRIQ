package com.vervyle.design_system.components

import android.graphics.BitmapFactory
import android.graphics.Point
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.theme.Theme

const val TAG: String = "TEST"

@Deprecated("Use StructuredImage instead")
@Composable
fun InteractableImage(
    backgroundImage: ImageBitmap,
    structuresList: List<ImageBitmap>,
    modifier: Modifier = Modifier
) {

    val pixelMapList = structuresList.map {
        it.toPixelMap()
    }

    var boxSize by remember {
        mutableStateOf(Pair(1f, 1f))
    }

    var shownSelections by remember {
        mutableStateOf(emptySet<ImageBitmap>())
    }

    LaunchedEffect(key1 = structuresList) {
        shownSelections = emptySet()
    }

    Box(
        modifier = Modifier
            .onSizeChanged { newSize ->
                val width = newSize.width.toFloat()
                val height = newSize.height.toFloat()
                boxSize = Pair(width, height)
            }
    ) {
        Image(
            bitmap = backgroundImage,
            contentDescription = null,
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            val relativeX = (offset.x / boxSize.first)
                            val relativeY = (offset.y / boxSize.second)
                            pixelMapList.forEachIndexed { index, pixelMap ->
                                val point = Point(
                                    (relativeX * structuresList[index].width).toInt(),
                                    (relativeY * structuresList[index].height).toInt()
                                )
                                if (pixelMap[point.x, point.y] != Color.Transparent) {
                                    val selection = structuresList[index]
                                    shownSelections = if (shownSelections.contains(selection)) {
                                        shownSelections - selection
                                    } else {
                                        shownSelections + selection
                                    }
                                }
                            }
                        })
                }
        )
        shownSelections.forEach { imageBitmap ->
            Image(
                bitmap = imageBitmap,
                contentDescription = null,
                modifier = modifier
            )
        }
    }
}

//@Preview
//@Composable
//private fun InteractableImagePreview(
//
//) {
//    val bitmap =
//        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.im_0001_0060)
//    Theme {
//        Background {
//            InteractableImage(
//                backgroundImage = bitmap.asImageBitmap(),
//                structuresList = emptyList(),
//                modifier = Modifier.size(5000.dp)
//            )
//        }
//    }
//}