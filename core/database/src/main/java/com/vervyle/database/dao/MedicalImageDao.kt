package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.vervyle.database.model.MedicalImageEntity
import com.vervyle.database.model.aggregates.MedicalImageWithAnnotations
import com.vervyle.model.Plane

@Dao
interface MedicalImageDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMedicalImage(medicalImage: MedicalImageEntity)

    @Upsert
    suspend fun upsertMedicalImage(medicalImage: MedicalImageEntity)

    @Delete
    suspend fun deleteMedicalImage(medicalImage: MedicalImageEntity)

    @Query("SELECT * FROM medical_images WHERE plane = :plane AND image_index = :index")
    suspend fun getImagesByPlaneAndIndex(plane: Plane, index: Int): List<MedicalImageEntity>

    @Query("SELECT * FROM medical_images WHERE plane = :plane")
    suspend fun getImagesByPlane(plane: Plane): List<MedicalImageEntity>

    @Query("SELECT * FROM medical_images WHERE image_index = :index")
    suspend fun getImagesByIndex(index: Int): List<MedicalImageEntity>

    @Query("SELECT * FROM medical_images")
    suspend fun getAllMedicalImages(): List<MedicalImageEntity>

    @Transaction
    @Query("SELECT * FROM medical_images WHERE id = :imageId")
    suspend fun getImageWithAnnotations(imageId: Int): MedicalImageWithAnnotations?

    @Transaction
    @Query("SELECT * FROM medical_images")
    suspend fun getAllImagesWithAnnotations(): List<MedicalImageWithAnnotations>
}