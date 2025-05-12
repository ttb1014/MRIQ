package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.vervyle.database.model.StructureGuessEntity
import kotlinx.datetime.Instant

@Dao
interface StructureGuessDao {

    @Transaction
    suspend fun getLocalIdAndInsert(guess: StructureGuessEntity) {
        val localId = getStructureIdByExternalId(guess.structureId)

        insertStructureGuess(
            StructureGuessEntity(
                structureId = localId,
                isRight = guess.isRight,
                timeStamp = guess.timeStamp
            )
        )
    }

    @Query("SELECT COUNT(*) FROM structures")
    suspend fun getStructuresNumber(): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStructureGuess(guess: StructureGuessEntity): Long

    @Query("SELECT id FROM structures WHERE external_id = :externalId")
    suspend fun getStructureIdByExternalId(externalId: Int): Int

    @Upsert
    suspend fun upsertStructureGuess(guess: StructureGuessEntity)

    @Delete
    suspend fun deleteStructureGuess(guess: StructureGuessEntity)

    @Query("DELETE FROM structure_guesses WHERE time_stamp < :timeStamp")
    suspend fun deleteGuessesBefore(timeStamp: Instant)

    @Query("SELECT * FROM structure_guesses")
    suspend fun getAllGuesses(): List<StructureGuessEntity>

    @Query("SELECT * FROM structure_guesses WHERE structure_id = :structureId")
    suspend fun getGuessesByStructureId(structureId: Int): List<StructureGuessEntity>

    @Query(
        """
    SELECT * FROM structure_guesses 
    ORDER BY time_stamp DESC 
    LIMIT :limit
"""
    )
    suspend fun getLatestGuesses(limit: Int): List<StructureGuessEntity>
}
