package com.vervyle.quiz

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vervyle.data.repository.QuizRecordsRepository
import com.vervyle.data.repository.QuizRepository
import com.vervyle.model.Plane
import com.vervyle.model.QuizScreenResource
import com.vervyle.model.StructureAnswerRecord
import com.vervyle.quiz.ui.QuizScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val quizRepository: QuizRepository,
    private val quizRecordsRepository: QuizRecordsRepository
) : ViewModel() {

    private lateinit var quizQuestionGenerator: QuizQuestionGenerator

    private val generatedQuestionsChannel = Channel<Int>(DEFAULT_CHANNEL_CAPACITY)

    var currentAnnotation by mutableIntStateOf(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<QuizScreenUiState> = savedStateHandle
        .getStateFlow<String?>(QUIZ_ID_ARG, null)
        .flatMapLatest { it ->
            if (it.isNullOrBlank()) {
                flowOf(QuizScreenUiState.Failed)
            } else {
                quizRepository.getQuizByName(it)
                    .map<QuizScreenResource, QuizScreenUiState> { quiz ->
                        if (quiz.annotatedImages.keys.toList().size != Plane.entries.size)
                            throw RuntimeException("Resource does not contain images for all planes")
                        quiz.annotatedImages.values.forEach { list ->
                            if (list.isEmpty()) throw RuntimeException("Resource size is 0")
                        }
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

    private val _activePlane = MutableStateFlow<Plane>(Plane.AXIAL)
    val activePlane = _activePlane.asStateFlow()

    private val _planeToIndexMapping =
        MutableStateFlow<Map<Plane, Int>>(Plane.entries.associateWith { 0 })
    val planeToIndexMapping = _planeToIndexMapping.asStateFlow()

    val shownAnnotations: MutableStateFlow<List<Int>> =
        MutableStateFlow(emptyList<Int>())

    init {
        viewModelScope.launch {
            uiState.filterIsInstance<QuizScreenUiState.Loaded>()
                .collect {
                    launchQuestionProducer()
                    val initialAnnotation = generatedQuestionsChannel.receive()
                    currentAnnotation = initialAnnotation
                }

        }
    }

    private fun CoroutineScope.launchQuestionProducer() = launch {
        while (isActive) {
            val nextVals = generateNextNQuestions(DEFAULT_CHANNEL_CAPACITY)
            nextVals.forEach {
                generatedQuestionsChannel.send(it)
            }
        }
    }

    private suspend fun generateNextNQuestions(n: Int): IntArray {
        val result = viewModelScope.async {
            val history = quizRecordsRepository.getAllAnswerRecords().first()
            val availableStructureNumber = quizRecordsRepository.getStructuresNumber().first()
            quizQuestionGenerator = QuizQuestionGenerator()
            quizQuestionGenerator.generateQuestions(history, availableStructureNumber, n)
        }
        return result.await()
    }

    fun onPlaneIndexChange(plane: Plane, index: Int) {
        val newMap = _planeToIndexMapping.value.toMutableMap()
        newMap[plane] = index
        _planeToIndexMapping.value = newMap
    }

    fun onActivePlaneChange(plane: Plane) {
        _activePlane.value = plane
    }

    fun onAnnotationClick(plane: Plane, imageIndex: Int, annotationIndex: Int) {
        val newShownAnnotations = shownAnnotations.value.toMutableList()
        if (shownAnnotations.value.contains(annotationIndex)) {
            newShownAnnotations.remove(annotationIndex)
            shownAnnotations.value = newShownAnnotations
            return
        }
        newShownAnnotations.add(annotationIndex)
        shownAnnotations.value = newShownAnnotations
        return
    }

    fun onUserInput(s: String) {
        viewModelScope.launch {
            val annotationId = currentAnnotation
            val correctName = quizRecordsRepository.getStructureNameById(annotationId).first()
            quizRecordsRepository.insertAnswerRecord(
                StructureAnswerRecord(
                    structureId = annotationId,
                    isCorrect = s.isCorrectTo(correctName),
                    timeStamp = Clock.System.now()
                )
            )
            currentAnnotation = generatedQuestionsChannel.receive()
        }
    }

    private fun String.isCorrectTo(other: String): Boolean {
        return this.lowercase() == other.lowercase()
    }

    companion object {
        private const val STATE_KEY = "quiz_state"
        private const val DEFAULT_CHANNEL_CAPACITY = 10
    }
}