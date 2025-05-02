package com.vervyle.local

import com.vervyle.model.QuizScreenResource
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getQuizScreenResourceByDatasetName(name: String): Flow<QuizScreenResource>

    fun insertQuizScreenResource(quizScreenResource: QuizScreenResource)
}