package com.vervyle.model

import kotlinx.datetime.Instant

data class StructureAnswerRecord(
    val structureId: Int,
    val isCorrect: Boolean,
    val timeStamp: Instant
)
