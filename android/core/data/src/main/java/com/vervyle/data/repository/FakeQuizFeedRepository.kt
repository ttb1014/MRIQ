package com.vervyle.data.repository

import com.vervyle.mock.userQuizResources
import com.vervyle.model.old.UserQuizResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeQuizFeedRepository @Inject constructor() : QuizFeedRepository {
    override fun observeAll(): Flow<List<UserQuizResource>> = flowOf(
        userQuizResources
    )
}