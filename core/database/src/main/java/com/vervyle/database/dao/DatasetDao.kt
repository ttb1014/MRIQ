package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.vervyle.database.model.DatasetEntity
import com.vervyle.database.model.aggregates.DatasetWithAnnotatedImages

@Dao
interface DatasetDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDataset(dataset: DatasetEntity)

    @Upsert
    suspend fun upsertDataset(dataset: DatasetEntity)

    @Delete
    suspend fun deleteDataset(dataset: DatasetEntity)

    @Query("DELETE FROM datasets WHERE name = :name")
    suspend fun deleteByName(name: String)

    @Query("SELECT * FROM datasets")
    suspend fun getAllDatasets(): List<DatasetEntity>

    @Query("SELECT * FROM datasets WHERE name = :name LIMIT 1")
    suspend fun getDatasetByName(name: String): DatasetEntity?

    @Transaction
    @Query("SELECT * FROM datasets WHERE name = :name LIMIT 1")
    suspend fun getDatasetWithAnnotatedImagesByName(name: String): DatasetWithAnnotatedImages?
}