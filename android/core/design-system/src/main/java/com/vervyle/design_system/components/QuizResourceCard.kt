package com.vervyle.design_system.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.theme.LocalTimeZone
import com.vervyle.model.old.MriData
import com.vervyle.model.old.Topic
import com.vervyle.model.old.UserQuizResource
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun QuizResourceCard(
    userQuizResource: UserQuizResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.outlinedCardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column {
            Box(
                modifier = Modifier.padding(16.dp),
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        Text(
                            userQuizResource.title,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = modifier
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        QuizResourceMetadata(userQuizResource.date)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun QuizResourceCardPreview() {
    QuizResourceCard(
        userQuizResource = UserQuizResource(
            id = 0,
            title = "title",
            mriData = MriData(emptyList(), emptyList(), emptyList()),
            pathToHeaderImage = "",
            date = Instant.DISTANT_FUTURE,
            topics = emptyList<Topic>(),
            isBookmarked = false,
            isViewed = false,
        ),
        onClick = {}
    )
}

@Composable
fun QuizResourceShortDescription(
    description: String,
) {
    Text(description, style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun QuizResourceMetadata(
    date: Instant,
    modifier: Modifier = Modifier
) {
    val formattedDate = dateFormatted(date)
    Text(
        text = formattedDate,
        style = MaterialTheme.typography.labelSmall,
    )
}

@Composable
fun dateFormatted(publishDate: Instant): String = DateTimeFormatter
    .ofLocalizedDate(FormatStyle.MEDIUM)
    .withLocale(Locale.getDefault())
    .withZone(LocalTimeZone.current.toJavaZoneId())
    .format(publishDate.toJavaInstant())