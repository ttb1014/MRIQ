package com.vervyle.quiz_feed

import com.vervyle.model.old.UserQuizResource

sealed interface QuizFeedState {

    data object Loading : QuizFeedState

    data class Success(
        val feed: List<UserQuizResource>,
    ) : QuizFeedState
}
