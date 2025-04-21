package com.vervyle.quiz_feed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.components.LoadingWheel
import com.vervyle.model.old.UserQuizResource

//@OptIn(ExperimentalFoundationApi::class)
//fun LazyListScope.QuizFeed(
//    quizFeedState: QuizFeedState,
//    onClick: (Int) -> Unit,
//    onTopicClick: (Int) -> Unit,
//    onToggleBookmark: (Int) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    when (quizFeedState) {
//        QuizFeedState.Loading ->
//            item { LoadingWheel() }
//
//        is QuizFeedState.Success -> {
//            items(
//                count = quizFeedState.feed.size,
//            ) { index ->
//                val context = LocalContext.current
//                QuizCard(
//                    quizResource = quizFeedState.feed[index],
//                    isBookmarked = true,
//                    hasBeenViewed = true,
//                    onClick = onClick,
//                    onTopicClick = onTopicClick,
//                    onToggleBookmark = onToggleBookmark,
//                    modifier = Modifier
//                        .padding(horizontal = 8.dp)
//                        .animateItemPlacement(),
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//    }
//}

//@Preview
//@Composable
//private fun QuizFeedPreview(
//    @PreviewParameter(UserQuizResourcePreviewParameterProvider::class)
//    userQuizResources: List<UserQuizResource>
//) {
//    LazyColumn {
//        QuizFeed(
//            quizFeedState = QuizFeedState.Success(
//                userQuizResources
//            ),
//            onClick = {},
//            onTopicClick = {},
//            onToggleBookmark = {})
//    }
//}