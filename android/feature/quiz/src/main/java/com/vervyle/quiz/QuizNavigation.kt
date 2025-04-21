package com.vervyle.quiz

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vervyle.design_system.components.LoadingWheel
import com.vervyle.quiz.ui.QuizScreen
import com.vervyle.quiz.ui.QuizScreenUiState

const val QUIZ_ROUTE_BASE = "quiz_route"
const val QUIZ_ID_ARG = "quiz_id"
const val QUIZ_ROUTE = "$QUIZ_ROUTE_BASE?$QUIZ_ID_ARG={$QUIZ_ID_ARG}"

fun NavController.navigateToQuiz(
    quizId: String? = null,
    navOptions: NavOptions? = null
) {
    val route = if (quizId != null) {
        "$QUIZ_ROUTE_BASE?$QUIZ_ID_ARG=$quizId"
    } else {
        QUIZ_ROUTE_BASE
    }
    navigate(route, navOptions)
}

fun NavGraphBuilder.quizScreen() {
    composable(
        route = QUIZ_ROUTE,
        arguments = listOf(
            navArgument(QUIZ_ID_ARG) {
                defaultValue = "dataset_brain_new"
                nullable = false
                type = NavType.StringType
            }
        )
    ) {
        QuizRoute()
    }
}

@Composable
internal fun QuizRoute(
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is QuizScreenUiState.Loaded -> QuizScreen(
            (uiState as QuizScreenUiState.Loaded).quizScreenResource
        )

        else -> {
            LoadingWheel(Modifier.size(160.dp))
        }
    }
}