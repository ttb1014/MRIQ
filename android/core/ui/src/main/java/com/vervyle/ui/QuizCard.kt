package com.vervyle.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.vervyle.design_system.components.IconToggleButton
import com.vervyle.design_system.components.Icons
import com.vervyle.design_system.components.Tag
import com.vervyle.design_system.effects.LoadingState
import com.vervyle.design_system.effects.ShimmerEffectItem
import com.vervyle.design_system.theme.LocalTimeZone
import com.vervyle.design_system.theme.Theme
import com.vervyle.model.Topic
import com.vervyle.model.UserQuizResource
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun QuizCard(
    quizResource: UserQuizResource,
    isBookmarked: Boolean,
    hasBeenViewed: Boolean,
    onClick: (Int) -> Unit,
    onToggleBookmark: (Int) -> Unit,
    onTopicClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = {
            onClick(quizResource.id)
        },
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column {
            HeaderImage(pathToImage = quizResource.pathToHeaderImage)
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Row {
                    Text(
                        text = quizResource.title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BookmarkButton(
                        isBookmarked = isBookmarked,
                        onClick = { /*TODO*/ })
                }
                Spacer(modifier = Modifier.height(14.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!hasBeenViewed) {
                        NotificationDot(
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(8.dp),
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                    }
                    MetaData(quizResource.date)
                }
                Spacer(modifier = Modifier.height(14.dp))
                NewsResourceTopics(
                    topics = quizResource.topics,
                    onTopicClick = onTopicClick,
                )
            }
        }
    }
}

@Composable
fun NewsResourceTopics(
    topics: List<Topic>,
    onTopicClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        for (topic in topics) {
            Tag(
                followed = true,
                onClick = { onTopicClick(topic.id) },
                text = {
                    Text(
                        text = topic.name.uppercase(Locale.getDefault()),
                    )
                },
            )
        }
    }
}

@Composable
fun dateFormatted(publishDate: Instant): String = DateTimeFormatter
    .ofLocalizedDate(FormatStyle.MEDIUM)
    .withLocale(Locale.getDefault())
    .withZone(LocalTimeZone.current.toJavaZoneId())
    .format(publishDate.toJavaInstant())

@Composable
fun MetaData(
    publishDate: Instant,
) {
    val formattedDate = dateFormatted(publishDate)
    Text(
        text = formattedDate,
        style = MaterialTheme.typography.labelSmall,
    )
}

@Composable
fun NotificationDot(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier,
        onDraw = {
            drawCircle(
                color,
                radius = size.minDimension / 2,
            )
        },
    )
}

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconToggleButton(
        checked = isBookmarked,
        onCheckedChange = { onClick() },
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.BookmarkBorder,
                contentDescription = null,
            )
        },
        checkedIcon = {
            Icon(
                imageVector = Icons.Bookmark,
                contentDescription = null,
            )
        },
    )
}

@Composable
fun HeaderImage(
    pathToImage: String,
    modifier: Modifier = Modifier,
) {
    var loadingState: LoadingState by remember {
        mutableStateOf(LoadingState.Loading)
    }
    val imageLoader = rememberAsyncImagePainter(
        model = pathToImage,
        onState = { state ->
            loadingState = when (state) {
                AsyncImagePainter.State.Empty -> LoadingState.Error
                is AsyncImagePainter.State.Error -> LoadingState.Error
                is AsyncImagePainter.State.Loading -> LoadingState.Loading
                is AsyncImagePainter.State.Success -> LoadingState.Loaded
            }
        },
    )
    val isLocalInspection = LocalInspectionMode.current
    ShimmerEffectItem(
        loadingState = loadingState,
        contentOnLoaded = {
            Image(
                painter = imageLoader,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        },
        contentOnError = { /*TODO*/ },
        modifier
    )
}

//@Preview
//@Composable
//private fun QuizCardPreview(
//    @PreviewParameter(UserQuizResourcePreviewParameterProvider::class)
//    quizResources: List<UserQuizResource>
//) {
//    Theme {
//        val quizResource = quizResources[0]
//        QuizCard(
//            quizResource = quizResource,
//            isBookmarked = quizResource.isBookmarked,
//            hasBeenViewed = quizResource.isViewed,
//            {},
//            {},
//            {}
//        )
//    }
//}