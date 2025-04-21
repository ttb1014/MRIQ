package com.vervyle.mock

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class DatasetEntityProvider(
    private val context: Context
) {

    private fun readRawResource(resourceId: Int): String {
        val inputStream = context.resources.openRawResource(resourceId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()

        reader.use {
            it.forEachLine { line ->
                stringBuilder.append(line).append("\n")
            }
        }

        return stringBuilder.toString()
    }


//    val mockDatasetWithAnnotatedImages = run {
//        DatasetWithAnnotatedImages(dataset = DatasetEntity(
//            id = 0,
//            name = "brain_mri",
//        ), annotatedImages = run {
//            val schema = readRawResource(R.raw.schema)
//            val images = mutableListOf<MedicalImageWithAnnotations>()
//
//            repeat(178) { id ->
//                val plane = Plane.AXIAL
//                images.add(
//                    MedicalImageWithAnnotations(image = MedicalImageEntity(
//                        id = id,
//                        imagePath = "${plane.name.lowercase()}_0000_${id.toImageNameTemplate()}",
//                        plane = plane,
//                        index = id
//                    ), annotations = run {
//                        val annotations = mutableListOf<AnnotationWithStructure>()
//
//                        annotations
//                    })
//                )
//            }
//            images
//        })
//    }
}