package com.vervyle.database.model.old

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "topics"
)
data class Topic(
    @PrimaryKey
    val id: Int,
    val content: String
)
