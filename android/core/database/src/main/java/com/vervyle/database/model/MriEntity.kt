package com.vervyle.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "mris"
)
data class MriEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val date: Instant
)