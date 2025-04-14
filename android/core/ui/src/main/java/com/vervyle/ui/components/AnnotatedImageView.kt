package com.vervyle.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AnnotatedImageView(
    image: Bitmap,
    annotation: Bitmap?,
    modifier: Modifier = Modifier
) {
    var isAnnotationShown by remember { mutableStateOf(true) }
    Box(
        modifier = modifier
    ) {
        Image(image.asImageBitmap(), null)
        annotation?.run {
            ClickableAnnotation(
                annotation.asImageBitmap(),
                { isAnnotationShown = !isAnnotationShown },
                isAnnotationShown
            )
        }
    }
}

@Preview
@Composable
private fun AnnotatedImageViewPreview() {
    val resources = LocalContext.current.resources
    AnnotatedImageView(
        BitmapFactory.decodeResource(resources, com.vervyle.mock.R.drawable.axial_0000_0062),
        BitmapFactory.decodeResource(resources, com.vervyle.mock.R.drawable.axial_0062_0001),
    )
}