package com.vervyle.data.repository

import com.vervyle.database.dao.StructureGuessDao
import com.vervyle.database.model.StructureGuessEntity
import com.vervyle.model.StructureAnswerRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class LocalQuizRecordsRepository @Inject constructor(
    private val structureGuessDao: StructureGuessDao,
) : QuizRecordsRepository {

    override fun getAllAnswerRecords(): Flow<List<StructureAnswerRecord>> = flow {
        val answers = structureGuessDao.getAllGuesses()
        emit(
            answers.map {
                it.asExternalModel()
            }
        )
    }

    private fun StructureGuessEntity.asExternalModel(): StructureAnswerRecord {
        return StructureAnswerRecord(
            structureId = this.structureId,
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
            structureGuessDao.insertStructureGuess(structureAnswerRecord.asLocalModel())
        }
    }

    // TODO: implement
    override fun getStructuresNumber(): Flow<Int> =
        flowOf(89)
}