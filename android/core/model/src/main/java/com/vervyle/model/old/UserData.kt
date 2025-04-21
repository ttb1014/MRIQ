package com.vervyle.model.old

data class UserData(
    val bookmarkedQuizResources: Set<Int>,
    val viewedQuizResources: Set<Int>,
    val shouldHideOnboarding: Boolean,
)
