package com.vervyle.data.repository

import com.vervyle.data.exceptions.FileNotParsedException
import com.vervyle.model.Plane
import com.vervyle.model.Structure
import com.vervyle.model.StructuredImage
import com.vervyle.model.StructuredMriData
import com.vervyle.network.MedExNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale
import javax.inject.Inject

class StructuredMriDataRepositoryImpl @Inject constructor(
    private val network: MedExNetworkDataSource
) : StructuredMriDataRepository {

    override fun getStructuredMriData(id: String): Flow<StructuredMriData> {
        return flow {
            val files = network.getZipTest(id)
            val data = parseAssets(files)
            emit(data)
        }
    }

    private suspend fun parseAssets(files: List<File>): StructuredMriData =
        withContext(Dispatchers.Default) {
            val schema = files.find { it.name == "mappings.txt" }
            val assetSchema = schema?.run {
                TODO()
            } ?: run {
                val assetsSchema = files
                    .filter { it.name != "mappings.txt" }
                    .map { file ->
                        getFileMetadata(file)
                    }

                Plane.entries.map { plane ->
                    Pair(plane, assetsSchema
                        .filter { fileMetadata ->
                            fileMetadata.baseFileId == "0000" && fileMetadata.plane == plane
                        }
                        .map { it.fileId }
                        .map { baseImageNameForPlane ->
                            Pair(baseImageNameForPlane,
                                assetsSchema
                                    .filter { fileMetadata ->
                                        fileMetadata.baseFileId == baseImageNameForPlane &&
                                                fileMetadata.plane == plane
                                    }
                                    .map { it.baseFileId })
                        })
                }
            }
            StructuredMriData(
                axial = assetSchema.find { it.first == Plane.AXIAL }?.run {
                    with(second) {
                        map { pair ->
                            StructuredImage(
                                imageName = pair.first,
                                structures = pair.second.map {
                                    Structure(it, it)
                                }
                            )
                        }
                    }
                } ?: throw FileNotParsedException("???"),
                coronal = assetSchema.find { it.first == Plane.CORONAL }?.run {
                    with(second) {
                        map { pair ->
                            StructuredImage(
                                imageName = pair.first,
                                structures = pair.second.map {
                                    Structure(it, it)
                                }
                            )
                        }
                    }
                } ?: throw FileNotParsedException("???"),
                sagittal = assetSchema.find { it.first == Plane.SAGITTAL }?.run {
                    with(second) {
                        map { pair ->
                            StructuredImage(
                                imageName = pair.first,
                                structures = pair.second.map {
                                    Structure(it, it)
                                }
                            )
                        }
                    }
                } ?: throw FileNotParsedException("???")
            )
        }

    private fun getFileMetadata(file: File): FileMetadata {
        val buffer = file.name.split("-", "_", ".")
        if (buffer.size != 4)
            throw FileNotParsedException("Corrupted dataset. Cannot split filename: ${file.name}")

        return FileMetadata(
            plane = Plane.valueOf(buffer[0].uppercase(Locale.ROOT)),
            baseFileId = buffer[1],
            fileId = buffer[2],
        )
    }

    private data class FileMetadata(
        val plane: Plane,
        val baseFileId: String,
        val fileId: String,
    )
}

//    fun asdsadas(id: String): Flow<StructuredMriData> {
//        return flow {
//            val files = network.getZipTest(id)
//            val mappingsFile = files.find { it.name == "mappings" }
//            val basesAxial = mutableMapOf<String, String>()
//            val basesCoronal = mutableMapOf<String, String>()
//            val basesSagittal = mutableMapOf<String, String>()
//            val structuresAxial = mutableMapOf<String, MutableList<Structure>>()
//            val structuresCoronal = mutableMapOf<String, MutableList<Structure>>()
//            val structuresSagittal = mutableMapOf<String, MutableList<Structure>>()
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
//                            basesAxial[metadata.id] = file.name
//                            return@forEach
//                        }
//                        if (!structuresAxial.containsKey(metadata.base))
//                            structuresAxial[metadata.base] = mutableListOf()
//                        structuresAxial[metadata.base]!!.add(Structure(metadata.id, file.name))
//                    }
//
//                    Plane.CORONAL -> {
//                        if (metadata.base == "0000") {
//                            if (basesCoronal.containsKey(metadata.id))
//                                throw RuntimeException("Corrupted dataset. Ambiguous file name in coronal frames: ${file.name}")
//                            basesCoronal[metadata.id] = file.name
//                            return@forEach
//                        }
//                        if (!structuresCoronal.containsKey(metadata.base))
//                            structuresCoronal[metadata.base] = mutableListOf()
//                        structuresCoronal[metadata.base]!!.add(Structure(metadata.id, file.name))
//                    }
//
//                    Plane.SAGITTAL -> {
//                        if (metadata.base == "0000") {
//                            if (basesSagittal.containsKey(metadata.id))
//                                throw RuntimeException("Corrupted dataset. Ambiguous file name in sagittal frames: ${file.name}")
//                            basesSagittal[metadata.id] = file.name
//                            return@forEach
//                        }
//                        if (!structuresSagittal.containsKey(metadata.base))
//                            structuresSagittal[metadata.base] = mutableListOf()
//                        structuresSagittal[metadata.base]!!.add(Structure(metadata.id, file.name))
//                    }
//                }
//            }
//            val data = StructuredMriData(
//                axial = basesAxial.map {
//                    StructuredImage(
//                        imageName = it.value,
//                        structures = structuresAxial[it.key] ?: emptyList()
//                    )
//                },
//                coronal = basesCoronal.map {
//                    StructuredImage(
//                        imageName = it.value,
//                        structures = structuresCoronal[it.key] ?: emptyList()
//                    )
//                },
//                sagittal = basesSagittal.map {
//                    StructuredImage(
//                        imageName = it.value,
//                        structures = structuresSagittal[it.key] ?: emptyList()
//                    )
//                },
//            )
//            emit(data)
//        }
//    }