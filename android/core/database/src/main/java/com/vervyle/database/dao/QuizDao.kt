package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.vervyle.database.model.MriEntity
import com.vervyle.database.model.PopulatedQuiz
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Query(
        value = """
        SELECT * FROM mri_bitmap
    """
    )
    fun getPopulatedQuizzes(): List<PopulatedQuiz>
}