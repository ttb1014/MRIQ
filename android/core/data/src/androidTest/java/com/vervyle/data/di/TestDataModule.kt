package com.vervyle.data.di

import com.vervyle.data.repository.OfflineFirstQuizRepository
import com.vervyle.data.repository.OfflineQuizRepository
import com.vervyle.data.repository.OnlineQuizRepository
import com.vervyle.data.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Named("offline")
    internal abstract fun bindsOfflineQuizRepository(
        offlineQuizRepository: OfflineQuizRepository
    ): QuizRepository

    @Binds
    @Named("offlineFirst")
    internal abstract fun bindsOfflineFirstQuizRepository(
        offlineFirstQuizRepository: OfflineFirstQuizRepository
    ): QuizRepository

    @Binds
    @Named("online")
    internal abstract fun bindsOnlineQuizRepository(
        onlineQuizRepository: OnlineQuizRepository
    ): QuizRepository
}