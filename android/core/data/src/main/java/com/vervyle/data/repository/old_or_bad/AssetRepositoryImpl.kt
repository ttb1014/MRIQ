package com.vervyle.data.repository.old_or_bad

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.vervyle.model.Plane
import com.vervyle.network.MriqNetworkDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Locale
import javax.inject.Inject

//const val TAG = "AssetRepository"
//
//class AssetRepositoryImpl @Inject constructor(
//    @ApplicationContext private val context: Context,
//    private val network: MriqNetworkDataSource
//) : AssetRepository {
//
//    private data class FileMetadata(
//        val plane: Plane,
//        val base: String,
//        val id: String
//    )
//
//    override fun getAssets(id: String): Flow<Asset> {
//        return flow {
//            val files = network.getDatasetImagesByName(id)
//            val mappingsFile = files.find { it.name == "mappings" }
//            val basesAxial = mutableMapOf<String, Bitmap>()
//            val basesCoronal = mutableMapOf<String, Bitmap>()
//            val basesSagittal = mutableMapOf<String, Bitmap>()
//            val structuresAxial = mutableMapOf<String, MutableList<Bitmap>>()
//            val structuresCoronal = mutableMapOf<String, MutableList<Bitmap>>()
//            val structuresSagittal = mutableMapOf<String, MutableList<Bitmap>>()
//            files.forEach { file ->
//                if (file.name == "mappings.txt")
//                    return@forEach
//
//                val metadata = getFileMetadata(file)
//                when (metadata.plane) {
//                    Plane.AXIAL -> {
//                        if (metadata.base == "0000") {
//                            if (basesAxial.containsKey(metadata.id))
//                                throw RuntimeException("Corrupted dataset. Ambiguous file name in axial frames: ${file.name}")
//                            basesAxial[metadata.id] = decodeJpg(file)
//                            return@forEach
//                        }
//                        if (!structuresAxial.containsKey(metadata.base))
//                            structuresAxial[metadata.base] = mutableListOf()
//                        structuresAxial[metadata.base]!!.add(decodePng(file))
//                    }
//
//                    Plane.CORONAL -> {
//                        if (metadata.base == "0000") {
//                            if (basesCoronal.containsKey(metadata.id))
//                                throw RuntimeException("Corrupted dataset. Ambiguous file name in coronal frames: ${file.name}")
//                            basesCoronal[metadata.id] = decodeJpg(file)
//                            return@forEach
//                        }
//                        if (!structuresCoronal.containsKey(metadata.base))
//                            structuresCoronal[metadata.base] = mutableListOf()
//                        structuresCoronal[metadata.base]!!.add(decodePng(file))
//                    }
//
//                    Plane.SAGITTAL -> {
//                        if (metadata.base == "0000") {
//                            if (basesSagittal.containsKey(metadata.id))
//                                throw RuntimeException("Corrupted dataset. Ambiguous file name in sagittal frames: ${file.name}")
//                            basesSagittal[metadata.id] = decodeJpg(file)
//                            return@forEach
//                        }
//                        if (!structuresSagittal.containsKey(metadata.base))
//                            structuresSagittal[metadata.base] = mutableListOf()
//                        structuresSagittal[metadata.base]!!.add(decodePng(file))
//                    }
//                }
//            }
//            val data = Asset(
//                axial = basesAxial.map {
//                    Pair(
//                        first = it.value,
//                        second = structuresAxial[it.key] ?: emptyList()
//                    )
//                },
//                coronal = basesCoronal.map {
//                    Pair(
//                        first = it.value,
//                        second = structuresCoronal[it.key] ?: emptyList()
//                    )
//                },
//                sagittal = basesSagittal.map {
//                    Pair(
//                        first = it.value,
//                        second = structuresSagittal[it.key] ?: emptyList()
//                    )
//                },
//            )
//            emit(data)
//        }
//    }
//
//    private fun getFileMetadata(file: File): FileMetadata {
//        val buffer = file.name.split("-", "_", ".")
//        if (buffer.size != 4) throw RuntimeException("Corrupted dataset. Cannot split filename: ${file.name}")
//        return FileMetadata(
//            plane = Plane.valueOf(buffer[0].uppercase(Locale.ROOT)),
//            base = buffer[1],
//            id = buffer[2],
//        )
//    }
//
//    private fun decodeJpg(file: File): Bitmap {
//        var fis: FileInputStream? = null
//        try {
//            fis = file.inputStream()
//        } catch (e: FileNotFoundException) {
//            Log.d(TAG, "file not found")
//            e.printStackTrace()
//        } catch (e: SecurityException) {
//            Log.d(TAG, "does not have permission to read")
//            e.printStackTrace()
//        }
//        val bitmap = BitmapFactory.decodeStream(fis)
//        fis?.close()
//        return bitmap
//    }
//
//    private fun decodePng(file: File): Bitmap {
//        var fis: FileInputStream? = null
//        try {
//            fis = file.inputStream()
//        } catch (e: FileNotFoundException) {
//            Log.d(TAG, "file not found")
//            e.printStackTrace()
//        } catch (e: SecurityException) {
//            Log.d(TAG, "does not have permission to read")
//            e.printStackTrace()
//        }
//        val bitmap = BitmapFactory.decodeStream(fis)
//        fis?.close()
//        return bitmap
//    }
//
//    // FIXME : throws an error
//    private fun decodeXml(file: File): Bitmap {
//        val drawable =
//            VectorDrawableCompat.createFromStream(file.inputStream(), null)
//                ?: run {
//                    Log.d(TAG, "decodeXml: ${file.readLines().map { it + "\n" }}")
//                    throw RuntimeException("")
//                }
//
//        val bitmap = Bitmap.createBitmap(
//            drawable.intrinsicWidth,
//            drawable.intrinsicHeight,
//            Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//
//        drawable.setBounds(0, 0, canvas.width, canvas.height)
//        drawable.draw(canvas)
//
//        return bitmap
//    }
//}