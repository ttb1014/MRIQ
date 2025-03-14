package com.vervyle.data.di

import com.vervyle.data.repository.AssetRepository
import com.vervyle.data.repository.AssetRepositoryImpl
import com.vervyle.data.repository.MriDataRepository
import com.vervyle.data.repository.MriDataRepositoryImpl
import com.vervyle.data.repository.QuizRepository
import com.vervyle.data.repository.QuizRepositoryImpl
import com.vervyle.data.repository.StructuredMriDataRepository
import com.vervyle.data.repository.StructuredMriDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsMriDataRepository(
        mriDataRepositoryImpl: MriDataRepositoryImpl
    ): MriDataRepository

    @Binds
    internal abstract fun bindsQuizRepository(
        quizRepositoryImpl: QuizRepositoryImpl
    ): QuizRepository

    @Binds
    internal abstract fun bindsStructuredMriDataRepository(
        structuredMriDataRepositoryImpl: StructuredMriDataRepositoryImpl
    ): StructuredMriDataRepository

    @Binds
    internal abstract fun bindsAssetRepository(
        assetRepositoryImpl: AssetRepositoryImpl
    ): AssetRepository
}