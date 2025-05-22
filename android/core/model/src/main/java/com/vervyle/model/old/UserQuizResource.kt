package com.vervyle.model.old

import kotlinx.datetime.Instant

data class UserQuizResource constructor(
    val id: Int,
    val title: String,
    val cleanTitle: String = title.split('_')
        .joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercaseChar() }
        },
    val mriData: MriData,
    val pathToHeaderImage: String,
    val date: Instant,
    val topics: List<Topic>,
    val isBookmarked: Boolean,
    val isViewed: Boolean,
) {
    constructor(quizResource: QuizResource, userData: UserData) : this(
        id = quizResource.id,
        title = quizResource.title,
        mriData = quizResource.mriData,
        pathToHeaderImage = quizResource.pathToHeaderImage,
        date = quizResource.date,
        topics = quizResource.topics,
        isBookmarked = quizResource.id in userData.viewedQuizResources,
        isViewed = quizResource.id in userData.viewedQuizResources
    )
}

fun List<QuizResource>.mapToUserQuizResources(userData: UserData): List<UserQuizResource> =
    map { UserQuizResource(it, userData) }
