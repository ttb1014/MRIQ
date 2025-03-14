package com.vervyle.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PopulatedQuiz(
    @Embedded
    val quiz: QuizBitmapCrossRef,
    @Relation(
        parentColumn = "mri_id",
        entityColumn = "id",
        associateBy = Junction(
            value = QuizTopicCrossRef::class,
            parentColumn = "quiz_id",
            entityColumn = "topic_id",
        )
    )
    val topics: List<Topic>
)
