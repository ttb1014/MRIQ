package com.vervyle.data.repository

import com.vervyle.model.QuizResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class QuizRepositoryImpl @Inject constructor(

) : QuizRepository {

    override fun getQuizzes(): Flow<List<QuizResource>> {
        TODO("Not yet implemented")
    }

    override fun getQuiz(id: Int): Flow<QuizResource> {
        TODO("Not yet implemented")
    }
}