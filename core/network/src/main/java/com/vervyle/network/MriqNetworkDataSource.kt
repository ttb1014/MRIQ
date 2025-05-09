package com.vervyle.network

import com.vervyle.network.model.QuizDto

/**
 * Внешний интерфейс, описывающий доступные функции для удаленного взаимодействия с сервером
 */
interface MriqNetworkDataSource {

    /**
     * Получение викторины по ID с сервера
     */
    suspend fun getQuizById(id: String): QuizDto
}