package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.vervyle.database.model.old.SliceEntity
import com.vervyle.database.model.old.BitmapsCrossRef
import com.vervyle.database.model.old.MriEntity

@Dao
interface SliceDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertSlice(sliceEntity: SliceEntity)

    @Upsert
    fun upsertSlice(sliceEntity: SliceEntity)

    @Delete
    fun deleteSlice(mriEntity: MriEntity)

    @Query("SELECT * from slices")
    fun getSlices() : List<SliceEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertSlicesCrossRef(bitmapsCrossRef: BitmapsCrossRef)

    @Upsert
    fun upsertSliceCrossRef(bitmapsCrossRef: BitmapsCrossRef)

    @Delete
    fun deleteSlicesCrossRef(bitmapsCrossRef: BitmapsCrossRef)

    @Query("SELECT * from selections")
    fun getSlicesCrossRefs() : List<BitmapsCrossRef>
}