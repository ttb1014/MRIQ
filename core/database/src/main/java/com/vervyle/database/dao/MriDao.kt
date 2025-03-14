package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.vervyle.database.model.MriBitmapCrossRef
import com.vervyle.database.model.MriEntity

@Dao
interface MriDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertMri(mriEntity: MriEntity)

    @Upsert
    fun upsertMri(mriEntity: MriEntity)

    @Delete
    fun deleteMri(mriEntity: MriEntity)

    @Query("SELECT * from mris")
    fun getMris(): List<MriEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertMriBitmapCrossRef(mriBitmapsCrossRef: MriBitmapCrossRef)

    @Upsert
    fun upsertMriBitmapsCrossRef(mriBitmapsCrossRef: MriBitmapCrossRef)

    @Delete
    fun deleteMriBitmapsCrossRef(mriBitmapsCrossRef: MriBitmapCrossRef)

    @Query("SELECT * from mri_bitmap")
    fun getMriBitmapCrossRefs(): List<MriBitmapCrossRef>
}