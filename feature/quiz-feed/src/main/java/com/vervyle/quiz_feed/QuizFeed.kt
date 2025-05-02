package com.vervyle.quiz_feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.components.QuizResourceCard

@Composable
fun QuizFeedScreen(
    feedState: QuizFeedState,
    onQuizClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isFeedLoading = feedState is QuizFeedState.Loading

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            quizFeed(
                feedState,
                onQuizClicked
            )
        }
    }
}

fun LazyListScope.quizFeed(
    feedState: QuizFeedState,
    onQuizClicked: (String) -> Unit,
) {
    when (feedState) {
        QuizFeedState.Loading -> Unit
        is QuizFeedState.Success -> {
            items(
                count = feedState.feed.size,
                key = { index ->
                    feedState.feed[index].title
                }
            ) { index ->
                val userQuizResource = feedState.feed[index]
                QuizResourceCard(
                    userQuizResource,
                    onClick = {
                        onQuizClicked(userQuizResource.title)
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}