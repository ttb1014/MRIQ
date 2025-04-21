package com.vervyle.local.di

import com.vervyle.local.LocalDataSource
import com.vervyle.local.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalStorageModule {

    @Binds
    internal abstract fun providesLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource
}