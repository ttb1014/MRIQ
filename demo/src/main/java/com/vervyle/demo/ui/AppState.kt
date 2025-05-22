package com.vervyle.demo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.vervyle.demo.navigation.TopLevelDestination
import com.vervyle.quiz.QUIZ_ROUTE
import com.vervyle.quiz.navigateToQuiz
import com.vervyle.quiz_feed.CATALOG_ROUTE
import com.vervyle.quiz_feed.navigateToQuizFeed
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navHostController: NavHostController = rememberNavController()
): AppState {
    return remember(
        coroutineScope,
        navHostController
    ) {
        AppState(
            coroutineScope,
            navHostController
        )
    }
}

@Stable
class AppState(
    private val coroutineScope: CoroutineScope,
    val navHostController: NavHostController
) {
    private val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val currentDestination: NavDestination?
        @Composable get() = navHostController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            CATALOG_ROUTE -> TopLevelDestination.CATALOG
            QUIZ_ROUTE -> TopLevelDestination.QUIZ
            else -> null
        }

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.CATALOG -> navHostController.navigateToQuizFeed(topLevelNavOptions)
            TopLevelDestination.QUIZ -> navHostController.navigateToQuiz()
        }
    }

    fun navigateToQuiz(quizId: String) {
        navHostController.navigateToQuiz(quizId)
    }

    fun popBackStack() {
        navHostController.popBackStack()
    }
}

