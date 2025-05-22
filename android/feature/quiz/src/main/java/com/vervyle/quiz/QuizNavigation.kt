package com.vervyle.quiz

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vervyle.design_system.components.ErrorInfoCard
import com.vervyle.design_system.components.Icons
import com.vervyle.design_system.components.LoadingWheel
import com.vervyle.quiz.ui.AnswerEvent
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
                nullable = true
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
    val shownStructures by viewModel.shownAnnotations.collectAsStateWithLifecycle()
    val currentAnnotationIndex = viewModel.currentAnnotation
    val activePlane by viewModel.activePlane.collectAsStateWithLifecycle()
    val planeToIndexMapping by viewModel.planeToIndexMapping.collectAsStateWithLifecycle()

    val answerToastOpacity by remember {
        mutableStateOf(
            Animatable(1f)
        )
    }
    var answerToastIcon: ImageVector? by remember {
        mutableStateOf(null)
    }
    var answerToastIconColor by remember {
        mutableStateOf(Color.Green)
    }
    LaunchedEffect(Unit) {
        viewModel.answerEvent.collect { event ->
            when (event) {
                AnswerEvent.Correct -> {
                    answerToastIcon = Icons.Correct
                    answerToastIconColor = Color.Green
                }

                AnswerEvent.Wrong -> {
                    answerToastIcon = Icons.Error
                    answerToastIconColor = Color.Red
                }
            }

            answerToastOpacity.snapTo(1f)
            answerToastOpacity.animateTo(
                targetValue = 0f,
                animationSpec = tween(2000),
            )
        }
    }

    when (uiState) {
        is QuizScreenUiState.Loaded -> {
            QuizScreen(
                quizScreenResource = (uiState as QuizScreenUiState.Loaded).quizScreenResource,
                shownStructures = shownStructures,
                activePlane = activePlane,
                planeToIndexMapping = planeToIndexMapping,
                quizzedStructure = currentAnnotationIndex,
                onUserInput = {
                    viewModel.onUserInput(it)
                },
                onPlaneChange = viewModel::onActivePlaneChange,
                onPlaneIndexChange = viewModel::onPlaneIndexChange,
                onAnnotationClick = viewModel::onAnnotationClick,
                answerToastOpacity = answerToastOpacity.value,
                answerToastIcon = answerToastIcon,
                answerToastIconColor = answerToastIconColor,
                modifier = Modifier,
            )
        }

        QuizScreenUiState.Loading ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                LoadingWheel(Modifier.size(160.dp))
            }

        QuizScreenUiState.Failed ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                ErrorInfoCard(
                    message = "Sorry, resource can't be loaded now",
                    onRetryClick = {},
                    modifier = Modifier
                )
            }
    }
}