package com.vervyle.demo.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import com.vervyle.demo.ui.AppState
import com.vervyle.quiz.QUIZ_ROUTE
import com.vervyle.quiz.navigateToQuiz
import com.vervyle.quiz.quizScreen
import com.vervyle.quiz_feed.navigateToQuizFeed
import com.vervyle.quiz_feed.quizFeedScreen

@Composable
fun NavigationHost(
    appState: AppState,
    navHostController: NavHostController,
    onShowSnackBar: suspend (String, String) -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = QUIZ_ROUTE,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        quizFeedScreen(
            appState::navigateToQuiz
        )
        quizScreen()
    }
}