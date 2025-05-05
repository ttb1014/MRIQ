package com.vervyle.local

import com.vervyle.model.QuizScreenResource
import kotlinx.coroutines.flow.Flow

@Deprecated("Do not use this module")
interface LocalDataSource {
    fun getQuizScreenResourceByDatasetName(name: String): Flow<QuizScreenResource>

    fun insertQuizScreenResource(quizScreenResource: QuizScreenResource)
}