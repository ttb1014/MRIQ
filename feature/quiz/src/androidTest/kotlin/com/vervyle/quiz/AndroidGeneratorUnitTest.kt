package com.vervyle.quiz

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AndroidGeneratorUnitTest {

    @Inject
    lateinit var questionGenerator: QuizQuestionGenerator

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun addition_isCorrect() {
        questionGenerator.generateQuestions(
            structureAnswerHistory = listOf(),
            availableStructureNumber = 96,
            z = 10
        )
    }
}