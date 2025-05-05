package com.vervyle.quiz

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vervyle.data.repository.QuizRecordsRepository
import com.vervyle.data.repository.QuizRepository
import com.vervyle.model.Plane
import com.vervyle.model.QuizScreenResource
import com.vervyle.quiz.ui.QuizScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val quizRepository: QuizRepository,
    private val quizRecordsRepository: QuizRecordsRepository
) : ViewModel() {

    private lateinit var quizQuestionGenerator: QuizQuestionGenerator

    private val generatedQuestionsChannel = Channel<Int>(10)

    val currentAnnotation = generatedQuestionsChannel
        .receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<QuizScreenUiState> = savedStateHandle
        .getStateFlow<String?>(QUIZ_ID_ARG, "")
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

    val indexToActiveStructuresMapping: MutableStateFlow<Map<Plane, List<List<Int>>>> =
        MutableStateFlow(emptyMap())

    init {
        viewModelScope.launch {
            while (isActive) {
                val nextVals = generateNextNQuestions(10)
                nextVals.forEach {
                    generatedQuestionsChannel.send(it)
                }
            }
        }
        viewModelScope.launch {
            uiState
                .filterIsInstance<QuizScreenUiState.Loaded>()
                .collect { loaded ->
                    indexToActiveStructuresMapping.value = Plane.entries.associateWith { plane ->
                        List(loaded.quizScreenResource.annotatedImages[plane]!!.size) { emptyList() }
                    }
                }
        }
    }

    private suspend fun generateNextNQuestions(n: Int): IntArray {
        val result = viewModelScope.async {
            val history = quizRecordsRepository.getAllAnswerRecords().first()
            val availableStructureNumber = 1
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
        val map = indexToActiveStructuresMapping.value.toMutableMap()
        val imageList = indexToActiveStructuresMapping.value[plane]!!.toMutableList()
        val annotationIndices =
            indexToActiveStructuresMapping.value[plane]!![imageIndex].toMutableList()
        if (annotationIndices.contains(annotationIndex)) {
            annotationIndices.remove(annotationIndex)
            imageList[imageIndex] = annotationIndices
            map[plane] = imageList
            indexToActiveStructuresMapping.value = map
            return
        }
        annotationIndices.add(annotationIndex)
        imageList[imageIndex] = annotationIndices
        map[plane] = imageList
        indexToActiveStructuresMapping.value = map
        return
    }

    fun onUserInput(s: String) {

    }
}