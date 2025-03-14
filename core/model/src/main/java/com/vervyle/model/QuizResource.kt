package com.vervyle.model

import kotlinx.datetime.Instant

data class QuizResource(
    val id: Int,
    val title: String,
    val mriData: MriData,
    val pathToHeaderImage: String,
    val date: Instant,
    val topics: List<Topic>,
) {
    constructor(userQuizResource: UserQuizResource) : this(
        id = userQuizResource.id,
        title = userQuizResource.title,
        mriData = userQuizResource.mriData,
        pathToHeaderImage = userQuizResource.title,
        date = userQuizResource.date,
        topics = userQuizResource.topics
    )
}

fun List<UserQuizResource>.mapToQuizResource(): List<QuizResource> =
    map {
        QuizResource(
            id = it.id,
            title = it.title,
            mriData = it.mriData,
            pathToHeaderImage = it.pathToHeaderImage,
            date = it.date,
            topics = it.topics
        )
    }