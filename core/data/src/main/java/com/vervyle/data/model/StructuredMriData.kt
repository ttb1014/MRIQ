package com.vervyle.data.model

//data class StructuredMriData(
//    val title: String,
//    val axialContent: List<Pair<String, List<String>>>,
//    val coronalContent: List<Pair<String, List<String>>>,
//    val sagittalContent: List<Pair<String, List<String>>>
//)
//
//fun PopulatedMri.asExternalModel() = StructuredMriData(
//    title = this.mriEntity.title,
//    axialContent = mriBitmaps
//        .filter { it.mainBitmapEntity.plane == Plane.AXIAL }
//        .map { populatedStructuredBitmap ->
//            Pair(populatedStructuredBitmap.mainBitmapEntity.fileName,
//                populatedStructuredBitmap.structures.map { it.fileName }
//            )
//        },
//    coronalContent = mriBitmaps
//        .filter { it.mainBitmapEntity.plane == Plane.CORONAL }
//        .map { populatedStructuredBitmap ->
//            Pair(populatedStructuredBitmap.mainBitmapEntity.fileName,
//                populatedStructuredBitmap.structures.map { it.fileName }
//            )
//        },
//    sagittalContent = mriBitmaps
//        .filter { it.mainBitmapEntity.plane == Plane.SAGITTAL }
//        .map { populatedStructuredBitmap ->
//            Pair(populatedStructuredBitmap.mainBitmapEntity.fileName,
//                populatedStructuredBitmap.structures.map { it.fileName }
//            )
//        },
//)