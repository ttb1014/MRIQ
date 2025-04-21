package com.vervyle.quiz.ui

import com.vervyle.model.QuizScreenResource

sealed interface QuizScreenUiState {

    data object Loading : QuizScreenUiState

    data object Failed : QuizScreenUiState

    data class Loaded(
        val quizScreenResource: QuizScreenResource
    ) : QuizScreenUiState
}