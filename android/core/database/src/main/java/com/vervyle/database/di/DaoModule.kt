package com.vervyle.database.di

import com.vervyle.database.MriqDatabase
import com.vervyle.database.dao.AggregatesDao
import com.vervyle.database.dao.AnnotationDao
import com.vervyle.database.dao.DatasetDao
import com.vervyle.database.dao.LinksDao
import com.vervyle.database.dao.MedicalImageDao
import com.vervyle.database.dao.StructureDao
import com.vervyle.database.dao.StructureGuessDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    fun provideAnnotationDao(
        database: MriqDatabase
    ): AnnotationDao = database.annotationDao()

    @Provides
    fun provideDatasetDao(database: MriqDatabase): DatasetDao = database.datasetDao()

    @Provides
    fun provideMedicalImageDao(database: MriqDatabase): MedicalImageDao = database.medicalImageDao()

    @Provides
    fun provideStructureDao(database: MriqDatabase): StructureDao = database.structureDao()

    @Provides
    fun provideStructureGuessDao(database: MriqDatabase): StructureGuessDao =
        database.structureGuessDao()

    @Provides
    fun provideLinksDao(database: MriqDatabase): LinksDao = database.linksDao()

    @Provides
    fun provideAggregatesDao(database: MriqDatabase): AggregatesDao = database.aggregatesDao()
}