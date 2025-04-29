package com.vervyle.mock.di

import android.content.Context
import com.vervyle.mock.MockAnnotatedImagesProvider
import com.vervyle.mock.UiResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MockDataModule {

    @Provides
    fun annotatedImagesProvider(
        @ApplicationContext context: Context
    ): MockAnnotatedImagesProvider {
        return MockAnnotatedImagesProvider(context)
    }


    @Provides
    fun uiResourceProvider(
        @ApplicationContext context: Context
    ): UiResourceProvider {
        return UiResourceProvider(
            MockAnnotatedImagesProvider(context)
        )
    }
}