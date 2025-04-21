package com.vervyle.database.di

import android.content.Context
import androidx.room.Room
import com.vervyle.database.MriqDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(
    SingletonComponent::class
)
object TestDatabaseModule {

    @Provides
    @Singleton
    @Named("test")
    internal fun provideInMemoryDb(@ApplicationContext context: Context): MriqDatabase {
        return Room.inMemoryDatabaseBuilder(context, MriqDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}