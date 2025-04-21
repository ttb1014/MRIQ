package com.vervyle.database.di

import android.content.Context
import androidx.room.Room
import com.vervyle.database.MriqDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Singleton
    @Provides
    fun providesMedExDatabase(
        @ApplicationContext context: Context
    ): MriqDatabase = Room.databaseBuilder(
        context,
        MriqDatabase::class.java,
        "mriq-database"
    )
        .fallbackToDestructiveMigration(true)
        .build()
}