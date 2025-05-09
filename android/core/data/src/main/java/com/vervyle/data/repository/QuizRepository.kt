package com.vervyle.data.repository

import com.vervyle.model.QuizScreenResource
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun getQuizByName(quizName: String): Flow<QuizScreenResource>
}