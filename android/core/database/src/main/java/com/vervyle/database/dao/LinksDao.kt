package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.vervyle.database.model.links.AnnotationStructureLink
import com.vervyle.database.model.links.DatasetImageLink
import com.vervyle.database.model.links.ImageAnnotationLink

@Dao
interface LinksDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAnnotationStructureLink(link: AnnotationStructureLink)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertImageAnnotationLink(link: ImageAnnotationLink)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDatasetImageLink(link: DatasetImageLink)
}