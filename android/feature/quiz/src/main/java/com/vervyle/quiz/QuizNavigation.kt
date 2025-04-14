package com.vervyle.quiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val QUIZ_ROUTE_BASE = "quiz_route"
const val QUIZ_ID_ARG = "quizId"
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

fun NavGraphBuilder.quizScreen(
) {
    composable(
        route = QUIZ_ROUTE,
        arguments = listOf(
            navArgument(QUIZ_ID_ARG) {
                defaultValue = null
                nullable = true
                type = NavType.StringType
            }
        )
    ) {
        QuizRoute(
        )
    }
}

@Composable
internal fun QuizRoute(
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    QuizScreen(
        uiState,
    )
}