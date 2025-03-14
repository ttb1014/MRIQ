package com.vervyle.design_system.components

import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.drawable.VectorDrawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.core.graphics.drawable.toBitmap

// FIXME: still does not scale properly
@Composable
fun StructuredImage(
    backgroundImage: ImageBitmap,
    structureImages: List<ImageBitmap>,
    shownStructuresIndexes: List<Int>,
    onStructureClick: (List<Int>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val structurePixelMapList = structureImages.map {
        it.toPixelMap()
    }

    var boxSize by remember {
        mutableStateOf(Pair(1f, 1f))
    }

    Box(
        modifier = Modifier,
        contentAlignment = Alignment.TopStart
    ) {
        Image(
            bitmap = backgroundImage,
            contentDescription = null,
            modifier = modifier
                .onSizeChanged { newSize ->
                    val width = newSize.width.toFloat()
                    val height = newSize.height.toFloat()
                    boxSize = Pair(width, height)
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            val clickedStructuresList: MutableList<Int> = mutableListOf()
                            val relativeX = (offset.x / boxSize.first)
                            val relativeY = (offset.y / boxSize.second)
                            structurePixelMapList.forEachIndexed { index, pixelMap ->
                                val structureImage = structureImages[index]
                                val point = Point(
                                    (relativeX * structureImage.width).toInt(),
                                    (relativeY * structureImage.height).toInt()
                                )
                                if (pixelMap[point.x, point.y] != Color.Transparent) {
                                    clickedStructuresList.add(index)
                                }
                            }
                            onStructureClick(clickedStructuresList)
                        })
                }
        )
        shownStructuresIndexes.forEach { index ->
            Image(
                bitmap = structureImages[index],
                contentDescription = null,
                modifier
            )
        }
    }
}

//@Preview
//@Composable
//private fun StructuredImagePreview() {
//    val bitmap =
//        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.im_0001_0060)
//    val structureImages = listOf(
//        LocalContext.current.getDrawable(R.drawable.sel_0001_0001_0060) as VectorDrawable,
//        LocalContext.current.getDrawable(R.drawable.sel_0002_0001_0060) as VectorDrawable,
//        LocalContext.current.getDrawable(R.drawable.sel_0003_0001_0060) as VectorDrawable,
//    )
//
//    StructuredImage(
//        backgroundImage = bitmap.asImageBitmap(),
//        structureImages = structureImages.map { it.toBitmap().asImageBitmap() },
//        onStructureClick = { list ->
//            Log.d(TAG, "StructuredImagePreview: clicked + ${list.map { it.toString() }}")
//        },
//        shownStructuresIndexes = listOf(0,1,2)
//    )
//}