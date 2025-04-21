package com.vervyle.local.di

import android.content.Context
import com.vervyle.local.disk.DiskFileManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DiskModule {

    @Provides
    internal fun providesDiskFileManager(
        @ApplicationContext context: Context
    ): DiskFileManager = DiskFileManager(context)
}