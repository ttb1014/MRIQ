package com.vervyle.demo.ui

import android.graphics.BitmapFactory
import android.graphics.drawable.VectorDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.vervyle.demo.R
import com.vervyle.design_system.components.InteractableImage

@Composable
fun TestComponent(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val vectorDrawableList = listOf(
        ResourcesCompat.getDrawable(
            context.resources,
            R.drawable.axial_0062_0001,
            context.theme
        ) as VectorDrawable
    )

    val backgroundImage = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.axial_0000_0062,
    )

    InteractableImage(
        backgroundImage = backgroundImage.asImageBitmap(),
        structuresList = vectorDrawableList.map {
            it.toBitmap().asImageBitmap()
        },
    )
}