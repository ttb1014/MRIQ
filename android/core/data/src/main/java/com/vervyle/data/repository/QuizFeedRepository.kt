package com.vervyle.data.repository

import com.vervyle.model.old.UserQuizResource
import kotlinx.coroutines.flow.Flow

interface QuizFeedRepository {

    fun observeAll(): Flow<List<UserQuizResource>>
}