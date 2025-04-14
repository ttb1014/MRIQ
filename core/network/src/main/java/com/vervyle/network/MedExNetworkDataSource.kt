package com.vervyle.network

import java.io.File

/**
 * Внешний интерфейс, описывающий доступные функции для удаленного взаимодействия с сервером
 */
interface MedExNetworkDataSource {

    suspend fun getDatasetImagesById(id: String? = null) : List<File>
}