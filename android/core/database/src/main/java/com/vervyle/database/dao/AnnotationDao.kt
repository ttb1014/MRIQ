package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.vervyle.database.model.AnnotationImageEntity
import com.vervyle.database.model.StructureEntity
import com.vervyle.database.model.aggregates.AnnotationWithStructure
import com.vervyle.database.model.links.AnnotationStructureLink

@Dao
interface AnnotationDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAnnotationImage(annotationImage: AnnotationImageEntity)

    @Upsert
    suspend fun upsertAnnotationImage(annotationImage: AnnotationImageEntity)

    @Delete
    suspend fun deleteAnnotationImage(annotationImage: AnnotationImageEntity)

    @Query("SELECT * FROM annotation_images")
    suspend fun getAllAnnotations(): List<AnnotationImageEntity>
}