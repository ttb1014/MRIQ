package com.vervyle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "quiz_topics",
    foreignKeys = [
        ForeignKey(
            entity = QuizBitmapCrossRef::class,
            parentColumns = ["mri_id"],
            childColumns = ["quiz_id"]
        ),
        ForeignKey(
            entity = Topic::class,
            parentColumns = ["id"],
            childColumns = ["topic_id"]
        )
    ]
)
data class QuizTopicCrossRef(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo("quiz_id")
    val quizBitmapCrossRefId: Int,
    @ColumnInfo("topic_id")
    val topicId: Int
)
