package com.vervyle.quiz.di

import com.vervyle.quiz.QuizQuestionGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TestModule {

    @Provides
    fun providesQuizQuestionGenerator(): QuizQuestionGenerator {
        return QuizQuestionGenerator()
    }
}