package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.vervyle.database.model.StructureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StructureDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStructure(structure: StructureEntity)

    @Upsert
    suspend fun upsertStructure(structure: StructureEntity)

    @Delete
    suspend fun deleteStructure(structure: StructureEntity)

    @Query("DELETE FROM structures WHERE name = :name")
    suspend fun deleteStructureByName(name: String)

    @Query("SELECT * FROM structures WHERE name = :name LIMIT 1")
    suspend fun getStructureByName(name: String): StructureEntity?

    @Query("SELECT * FROM structures")
    suspend fun getAllStructures(): List<StructureEntity>

    @Query("SELECT name FROM structures WHERE external_id = :id LIMIT 1")
    fun getStructureNameById(id: Int): Flow<String>
}