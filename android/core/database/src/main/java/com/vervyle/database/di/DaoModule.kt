package com.vervyle.database.di

import com.vervyle.database.MedExDatabase
import com.vervyle.database.dao.SliceDao
import com.vervyle.database.dao.MriDao
import com.vervyle.database.dao.QuizDao
import com.vervyle.database.dao.TopicDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    fun providesBitmapDao(
        database: MedExDatabase
    ) : SliceDao = database.bitmapDao()

    @Provides
    fun providesMriDao(
        database: MedExDatabase
    ) : MriDao = database.mriDao()

    @Provides
    fun providesTopicDao(
        database: MedExDatabase
    ) : TopicDao = database.topicDao()

    @Provides
    fun providesQuizDao(
        database: MedExDatabase
    ) : QuizDao = database.quizDao()
}