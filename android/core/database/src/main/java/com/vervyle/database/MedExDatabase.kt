package com.vervyle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vervyle.database.dao.SliceDao
import com.vervyle.database.dao.MriDao
import com.vervyle.database.dao.QuizDao
import com.vervyle.database.dao.TopicDao
import com.vervyle.database.model.SliceEntity
import com.vervyle.database.model.BitmapsCrossRef
import com.vervyle.database.model.MriBitmapCrossRef
import com.vervyle.database.model.MriEntity
import com.vervyle.database.model.QuizBitmapCrossRef
import com.vervyle.database.model.QuizTopicCrossRef
import com.vervyle.database.model.Topic
import com.vervyle.database.util.Converters

@Database(
    entities = [
        SliceEntity::class,
        BitmapsCrossRef::class,
        MriBitmapCrossRef::class,
        MriEntity::class,
        QuizBitmapCrossRef::class,
        QuizTopicCrossRef::class,
        Topic::class,
    ],
    version = 1,
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2, spec = DatabaseMigrations.Schema1to2::class),
//    ]
)
@TypeConverters(Converters::class)
internal abstract class MedExDatabase : RoomDatabase() {

    abstract fun bitmapDao(): SliceDao

    abstract fun mriDao(): MriDao

    abstract fun topicDao(): TopicDao

    abstract fun quizDao(): QuizDao
}