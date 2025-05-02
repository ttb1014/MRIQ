package com.vervyle.data.repository

import com.vervyle.model.StructureAnswerRecord
import kotlinx.coroutines.flow.Flow

interface QuizRecordsRepository {
    fun getAllAnswerRecords(): Flow<List<StructureAnswerRecord>>

    fun insertAnswerRecord(structureAnswerRecord: StructureAnswerRecord)
}