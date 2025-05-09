package com.vervyle.data.di

import com.vervyle.data.repository.FakeQuizFeedRepository
import com.vervyle.data.repository.LocalQuizRecordsRepository
import com.vervyle.data.repository.OfflineFirstQuizRepository
import com.vervyle.data.repository.QuizFeedRepository
import com.vervyle.data.repository.QuizRecordsRepository
import com.vervyle.data.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsQuizRepository(
        offlineFirstQuizRepository: OfflineFirstQuizRepository
    ): QuizRepository

    @Binds
    internal abstract fun bindsQuizFeedRepository(
        fakeQuizFeedRepository: FakeQuizFeedRepository
    ): QuizFeedRepository

    @Binds
    internal abstract fun bindsQuizRecordRepository(
        localQuizRecordsRepository: LocalQuizRecordsRepository
    ): QuizRecordsRepository
}