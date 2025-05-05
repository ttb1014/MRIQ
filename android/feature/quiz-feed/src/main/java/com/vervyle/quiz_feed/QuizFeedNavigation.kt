package com.vervyle.quiz_feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val CATALOG_ROUTE = "catalog_route"

fun NavController.navigateToQuizFeed(navOptions: NavOptions?) =
    navigate(CATALOG_ROUTE, navOptions)

fun NavGraphBuilder.quizFeedScreen(
    onQuizClicked: (String) -> Unit,
) {
    composable(route = CATALOG_ROUTE) {
        QuizFeedRoute(onQuizClicked)
    }
}

@Composable
internal fun QuizFeedRoute(
    onQuizClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuizFeedViewModel = hiltViewModel(),
) {
    val quizFeedState by viewModel.quizFeedState.collectAsStateWithLifecycle()

    QuizFeedScreen(
        feedState = quizFeedState,
        onQuizClicked = onQuizClicked,
        modifier = Modifier
    )
}