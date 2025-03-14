package com.vervyle.ui.state

import com.vervyle.model.UserQuizResource

sealed interface QuizFeedState {

    data object Loading : QuizFeedState

    data class Success(
        val feed: List<UserQuizResource>,
    ) : QuizFeedState
}
