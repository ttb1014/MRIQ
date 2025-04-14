package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.vervyle.database.model.old.PopulatedQuiz

@Dao
interface QuizDao {

    @Query(
        value = """
        SELECT * FROM mri_bitmap
    """
    )
    fun getPopulatedQuizzes(): List<PopulatedQuiz>
}