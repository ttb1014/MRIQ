package com.vervyle.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.input.pointer.pointerInput


@Composable
fun ClickableAnnotation(
    image: ImageBitmap,
    onClick: () -> Unit,
    isAnnotationShown: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .pointerInput(image) {
                detectTapGestures { offset ->
                    val pixelColor =
                        image.toPixelMap()[offset.x.toInt(), offset.y.toInt()].toArgb()
                    if (pixelColor != 0x00000000) {
                        onClick()
                    }
                }
            }
    ) {
        if (isAnnotationShown)
            Image(
                painter = BitmapPainter(image),
                contentDescription = null
            )
    }
}
