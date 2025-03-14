package com.vervyle.data.repository

import com.vervyle.model.QuizResource
import kotlinx.coroutines.flow.Flow

interface QuizRepository {

    fun getQuizzes(): Flow<List<QuizResource>>

    fun getQuiz(id: Int): Flow<QuizResource>
}