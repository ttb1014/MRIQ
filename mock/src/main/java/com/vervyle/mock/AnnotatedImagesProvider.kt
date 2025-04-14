package com.vervyle.mock

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.vervyle.mock.util.toImageNameTemplate
import com.vervyle.model.Plane
import java.util.Locale

class AnnotatedImagesProvider(
    private val context: Context
) {

    @SuppressLint("DiscouragedApi")
    fun providesAnnotatedImages(plane: Plane): List<Pair<Bitmap, Map<String, Bitmap>>> {
        val planeName = plane.name.lowercase()
        val images = mutableListOf<Pair<Bitmap, Map<String, Bitmap>>>()
        for (i in 1..500) {
            val baseImageName = "${planeName}_0000_${i.toImageNameTemplate()}"
            val baseImageId =
                context.resources.getIdentifier(baseImageName, "drawable", context.packageName)
            if (baseImageId == 0)
                continue

            val baseBitmap = BitmapFactory.decodeResource(context.resources, baseImageId)
            val annotationsMap = getAnnotationsMap(Plane.AXIAL, i.toImageNameTemplate())
            images.add(Pair(baseBitmap, annotationsMap))
        }
        return images
    }

    @SuppressLint("DiscouragedApi")
    private fun getAnnotationsMap(
        plane: Plane,
        baseImageIndex: String,
    ): Map<String, Bitmap> {
        val annotationsMap = mutableMapOf<String, Bitmap>()
        for (j in 1..1) {
            val annotationImageName =
                "${plane.name.lowercase(Locale.ROOT)}_${baseImageIndex}_${j.toImageNameTemplate()}"
            val annotationImageId =
                context.resources.getIdentifier(
                    annotationImageName,
                    "drawable",
                    context.packageName
                )
            if (annotationImageId == 0)
                continue
            val annotationBitmap =
                BitmapFactory.decodeResource(context.resources, annotationImageId)
            annotationsMap[j.toImageNameTemplate()] = annotationBitmap
        }
        return annotationsMap
    }
}