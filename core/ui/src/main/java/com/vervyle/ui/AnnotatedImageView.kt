package com.vervyle.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vervyle.design_system.modifiers.clickableAnnotation
import com.vervyle.design_system.modifiers.clickableAnnotations
import com.vervyle.mock.R

@Composable
fun AnnotatedImageView(
    image: Bitmap,
    annotation: Bitmap?,
    modifier: Modifier = Modifier
) {
    var isAnnotationShown by remember { mutableStateOf(true) }
    annotation?.let {
        Box(
            modifier = modifier
                .clickableAnnotation(annotation) {
                    isAnnotationShown = !isAnnotationShown
                }
        ) {
            Image(image.asImageBitmap(), null)
            if (isAnnotationShown) {
                Image(
                    annotation.asImageBitmap(),
                    null,
                    Modifier
                )
            }

        }
    }
}

@Composable
fun AnnotatedImageView(
    image: Bitmap,
    annotations: List<Bitmap>,
    modifier: Modifier = Modifier
) {
    var shownAnnotations by remember { mutableStateOf(annotations.indices.toList()) }

    val toggleImageIndex: (index: Int) -> Unit = { index ->
        shownAnnotations = if (shownAnnotations.contains(index)) {
            shownAnnotations.filter { it != index }
        } else {
            shownAnnotations + index
        }
    }

    Box(
        modifier.clickableAnnotations(
            annotations
        ) {
            toggleImageIndex(it)
        }
    ) {
        Image(image.asImageBitmap(), null, modifier.aspectRatio(1f))
        shownAnnotations.forEach { index ->
            Image(
                annotations[index].asImageBitmap(),
                null,
                modifier.aspectRatio(1f)
            )
        }
    }
}

@Preview
@Composable
private fun AnnotatedImageViewPreview() {
    val resources = LocalContext.current.resources
    AnnotatedImageView(
        BitmapFactory.decodeResource(resources, R.drawable.axial_0000_0062),
        BitmapFactory.decodeResource(resources, R.drawable.axial_0062_0001),
    )
}