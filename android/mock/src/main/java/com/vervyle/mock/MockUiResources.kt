package com.vervyle.mock

import com.vervyle.model.old.MriData
import com.vervyle.model.old.Topic
import com.vervyle.model.old.UserQuizResource
import kotlinx.datetime.Instant

val userQuizResource = UserQuizResource(
    id = 0,
    title = "dataset_brain_new",
    mriData = MriData(emptyList(), emptyList(), emptyList()),
    pathToHeaderImage = "",
    date = Instant.DISTANT_FUTURE,
    topics = emptyList<Topic>(),
    isBookmarked = false,
    isViewed = false,
)

val userQuizResources = listOf(
    UserQuizResource(
        id = 0,
        title = "dataset_brain_new",
        mriData = MriData(emptyList(), emptyList(), emptyList()),
        pathToHeaderImage = "",
        date = Instant.DISTANT_FUTURE,
        topics = emptyList<Topic>(),
        isBookmarked = false,
        isViewed = false,
    ),
    UserQuizResource(
        id = 0,
        title = "dataset_brain",
        mriData = MriData(emptyList(), emptyList(), emptyList()),
        pathToHeaderImage = "",
        date = Instant.DISTANT_FUTURE,
        topics = emptyList<Topic>(),
        isBookmarked = false,
        isViewed = false,
    ),
)