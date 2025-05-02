package com.vervyle.data.di

import com.vervyle.data.repository.FakeQuizFeedRepository
import com.vervyle.data.repository.LocalQuizRecordsRepository
import com.vervyle.data.repository.OfflineFirstQuizRepository
import com.vervyle.data.repository.QuizFeedRepository
import com.vervyle.data.repository.QuizRecordsRepository
import com.vervyle.data.repository.QuizRepository
import com.vervyle.data.repository.old_or_bad.AssetRepository
import com.vervyle.data.repository.old_or_bad.AssetRepositoryImpl
import com.vervyle.data.repository.old_or_bad.StructuredMriDataRepository
import com.vervyle.data.repository.old_or_bad.StructuredMriDataRepositoryImpl
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

//    @Binds
//    internal abstract fun bindsQuizRepository(
//        fakeQuizRepository: FakeQuizRepository
//    ): QuizRepository

    @Binds
    internal abstract fun bindsStructuredMriDataRepository(
        structuredMriDataRepositoryImpl: StructuredMriDataRepositoryImpl
    ): StructuredMriDataRepository

    @Binds
    internal abstract fun bindsAssetRepository(
        assetRepositoryImpl: AssetRepositoryImpl
    ): AssetRepository

    @Binds
    internal abstract fun bindsQuizFeedRepository(
        fakeQuizFeedRepository: FakeQuizFeedRepository
    ): QuizFeedRepository

    @Binds
    internal abstract fun bindsQuizRecordRepository(
        localQuizRecordsRepository: LocalQuizRecordsRepository
    ): QuizRecordsRepository
}