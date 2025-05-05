package com.vervyle.disk.di

import android.content.Context
import com.vervyle.disk.DiskFileManagerImpl
import com.vervyle.disk.DiskManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DiskModule {

    @Provides
    internal fun providesDiskFileManager(
        @ApplicationContext context: Context
    ): DiskManager = DiskFileManagerImpl(context)
}