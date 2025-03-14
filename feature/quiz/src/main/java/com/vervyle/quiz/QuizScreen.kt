package com.vervyle.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.components.LoadingWheel
import com.vervyle.design_system.components.QuizView

@Composable
fun QuizScreen(
    uiState: QuizScreenUiState,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is QuizScreenUiState.Loaded ->
            QuizView(
                axialStructuredBitmaps = uiState.axialStructuredBitmaps,
                coronalStructuredBitmaps = uiState.coronalStructuredBitmaps,
                sagittalStructuredBitmaps = uiState.sagittalStructuredBitmaps,
                modifier = modifier.fillMaxSize()
            )

        else ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize()
            ) {
                LoadingWheel(modifier = Modifier.size(160.dp))
            }
    }
}

//@Preview
//@Composable
//private fun QuizScreenPreview() {
//    val bitmap =
//        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.im_0001_0060)
//    val structureImages = listOf(
//        LocalContext.current.getDrawable(R.drawable.sel_0001_0001_0060) as VectorDrawable,
//        LocalContext.current.getDrawable(R.drawable.sel_0002_0001_0060) as VectorDrawable,
//        LocalContext.current.getDrawable(R.drawable.sel_0003_0001_0060) as VectorDrawable,
//    )
//    val axialData = listOf(
//        Pair(bitmap,
//            structureImages.map { it.toBitmap() }
//        ),
//        Pair(bitmap,
//            structureImages.map { it.toBitmap() }
//        )
//    )
//    QuizScreen(
//        uiState = QuizScreenUiState.Loaded(
//            axialData,
//            axialData,
//            axialData
//        )
//    )
//}