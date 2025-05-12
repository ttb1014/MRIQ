package com.vervyle.data.repository

import com.vervyle.database.dao.StructureDao
import com.vervyle.database.dao.StructureGuessDao
import com.vervyle.database.model.StructureEntity
import com.vervyle.database.model.StructureGuessEntity
import com.vervyle.model.StructureAnswerRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class LocalQuizRecordsRepository @Inject constructor(
    private val structureGuessDao: StructureGuessDao,
    private val structureDao: StructureDao
) : QuizRecordsRepository {

    override fun getAllAnswerRecords(): Flow<List<StructureAnswerRecord>> = flow {
        val answers = structureGuessDao.getAllGuesses()
        val structures = structureDao.getAllStructures()
        emit(
            answers.map {
                it.asExternalModel(structures)
            }
        )
    }

    private fun StructureGuessEntity.asExternalModel(structures: List<StructureEntity>): StructureAnswerRecord {
        return StructureAnswerRecord(
            structureId = structures.first { it.id == this.structureId }.externalId,
            isCorrect = this.isRight,
            timeStamp = this.timeStamp
        )
    }

    private fun StructureAnswerRecord.asLocalModel(): StructureGuessEntity {
        return StructureGuessEntity(
            structureId = structureId,
            isRight = isCorrect,
            timeStamp = timeStamp
        )
    }

    override fun insertAnswerRecord(structureAnswerRecord: StructureAnswerRecord) {
        CoroutineScope(Dispatchers.IO).launch {
            structureGuessDao.getLocalIdAndInsert(structureAnswerRecord.asLocalModel())
        }
    }

    override fun getStructuresNumber(): Flow<Int> = flow {
        emit(structureGuessDao.getStructuresNumber())
    }

    override fun getStructureNameById(id: Int): Flow<String> {
        return structureDao.getStructureNameById(id)
    }
}