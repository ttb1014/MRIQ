package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.vervyle.database.model.StructureGuessEntity
import kotlinx.datetime.LocalDateTime

@Dao
interface StructureGuessDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStructureGuess(guess: StructureGuessEntity)

    @Upsert
    suspend fun upsertStructureGuess(guess: StructureGuessEntity)

    @Delete
    suspend fun deleteStructureGuess(guess: StructureGuessEntity)

    @Query("DELETE FROM structure_guesses WHERE timeStamp < :timeStamp")
    suspend fun deleteGuessesBefore(timeStamp: LocalDateTime)

    @Query("SELECT * FROM structure_guesses")
    suspend fun getAllGuesses(): List<StructureGuessEntity>

    @Query("SELECT * FROM structure_guesses WHERE structure_id = :structureId")
    suspend fun getGuessesByStructureId(structureId: Int): List<StructureGuessEntity>

    @Query(
        """
    SELECT * FROM structure_guesses 
    ORDER BY timeStamp DESC 
    LIMIT :limit
"""
    )
    suspend fun getLatestGuesses(limit: Int): List<StructureGuessEntity>
}
