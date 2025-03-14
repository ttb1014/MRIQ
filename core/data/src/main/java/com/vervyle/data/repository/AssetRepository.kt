package com.vervyle.data.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface AssetRepository {

    fun getAssets(id: String): Flow<Asset>
}

data class Asset(
    val axial: List<Pair<Bitmap, List<Bitmap>>>,
    val coronal: List<Pair<Bitmap, List<Bitmap>>>,
    val sagittal: List<Pair<Bitmap, List<Bitmap>>>,
)