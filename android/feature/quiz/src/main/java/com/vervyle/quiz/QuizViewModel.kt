package com.vervyle.quiz

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vervyle.data.repository.QuizRepository
import com.vervyle.model.QuizScreenResource
import com.vervyle.quiz.ui.QuizScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val quizRepository: QuizRepository,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<QuizScreenUiState> = savedStateHandle
        .getStateFlow<String?>(QUIZ_ID_ARG, "")
        .flatMapLatest {
            if (it.isNullOrBlank()) {
                flowOf(QuizScreenUiState.Failed)
            } else {
                quizRepository.getQuizByName(it)
                    .map<QuizScreenResource, QuizScreenUiState> { quiz ->
                        QuizScreenUiState.Loaded(
                            quiz
                        )
                    }
                    .catch {
                        emit(QuizScreenUiState.Failed)
                    }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            QuizScreenUiState.Loading
        )
}