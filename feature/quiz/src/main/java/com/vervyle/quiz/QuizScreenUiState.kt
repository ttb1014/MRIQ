package com.vervyle.quiz

import android.graphics.Bitmap
import com.vervyle.model.StructuredMriData

sealed interface QuizScreenUiState {

    data object Loading : QuizScreenUiState

    data object Failed : QuizScreenUiState

    data class Loaded(
        val axialStructuredBitmaps: List<Pair<Bitmap, List<Bitmap>>>,
        val coronalStructuredBitmaps: List<Pair<Bitmap, List<Bitmap>>>,
        val sagittalStructuredBitmaps: List<Pair<Bitmap, List<Bitmap>>>,
    ) : QuizScreenUiState
}