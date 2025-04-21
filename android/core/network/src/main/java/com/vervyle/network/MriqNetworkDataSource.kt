package com.vervyle.network

import com.vervyle.model.QuizScreenResource
import java.io.File

/**
 * Внешний интерфейс, описывающий доступные функции для удаленного взаимодействия с сервером
 */
interface MriqNetworkDataSource {

    suspend fun getDatasetImagesByName(name: String? = null): List<File>

    /**
     * work in progress
     */
    suspend fun getImageByName(name: String): File
}