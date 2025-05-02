package com.vervyle.quiz_feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vervyle.data.repository.QuizFeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class QuizFeedViewModel @Inject constructor(
    private val quizFeedRepository: QuizFeedRepository,
) : ViewModel() {

    val quizFeedState: StateFlow<QuizFeedState> =
        quizFeedRepository.observeAll()
            .map(
                QuizFeedState::Success
            ).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = QuizFeedState.Loading,
            )
}
