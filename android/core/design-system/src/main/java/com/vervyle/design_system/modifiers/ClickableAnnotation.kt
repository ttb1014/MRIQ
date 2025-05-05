package com.vervyle.design_system.modifiers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColor
import com.vervyle.mock.R

fun Modifier.clickableAnnotation(
    annotation: Bitmap,
    onClick: () -> Unit,
) = this.pointerInput(annotation) {
    detectTapGestures(
        onTap = { offset ->
            val x = (offset.x / size.width * annotation.width).toInt()
            val y = (offset.y / size.height * annotation.height).toInt()
            val pixelArgb = annotation.getPixel(x, y).toColor().toArgb()
            if (pixelArgb != Color.Transparent.toArgb())
                onClick()
        }
    )
}

//fun Modifier.clickableAnnotations(
//    annotations: List<Bitmap>,
//    onClick: (Int) -> Unit
//) = this.pointerInput(annotations) {
//    detectTapGestures(
//        onTap = { offset ->
//            annotations.forEachIndexed { index, annotation ->
//                val x = (offset.x / size.width * annotation.width).toInt()
//                val y = (offset.y / size.height * annotation.height).toInt()
//                val pixelArgb = annotation.getPixel(x, y).toColor().toArgb()
//                if (pixelArgb != Color.Transparent.toArgb()) {
//                    onClick(index)
//                }
//            }
//        }
//    )
//}

fun Modifier.clickableAnnotations(
    annotations: List<Pair<Int, Bitmap>>,
    onClick: (Int) -> Unit
) = this.pointerInput(annotations) {
    detectTapGestures(
        onTap = { offset ->
            annotations.forEach { (index, annotation) ->
                val x = (offset.x / size.width * annotation.width).toInt()
                val y = (offset.y / size.height * annotation.height).toInt()
                val pixelArgb = annotation.getPixel(x, y).toColor().toArgb()
                if (pixelArgb != Color.Transparent.toArgb()) {
                    onClick(index)
                }
            }
        }
    )
}

@Preview
@Composable
private fun ClickableAnnotationPreview() {
    val context = LocalContext.current
    val testImage = BitmapFactory.decodeResource(context.resources, R.drawable.sagittal_0000_0109)
    val testAnnotation =
        BitmapFactory.decodeResource(context.resources, R.drawable.sagittal_0109_0001)
    var isShown by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .clickableAnnotation(testAnnotation) {
                isShown = !isShown
            }
    ) {
        Image(testImage.asImageBitmap(), null)
        if (isShown) {
            Image(testAnnotation.asImageBitmap(), null)
        }
    }
}