package com.vervyle.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.vervyle.database.model.old.Topic

@Dao
interface TopicDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTopic(topic: Topic)

    @Upsert
    fun upsertTopic(topic: Topic)

    @Delete
    fun deleteTopic(topic: Topic)

    @Query("SELECT * from topics")
    fun getTopics() : List<Topic>
}