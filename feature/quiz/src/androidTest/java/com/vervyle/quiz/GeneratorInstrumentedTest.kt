package com.vervyle.quiz

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import com.vervyle.model.StructureAnswerRecord

//@RunWith(RobolectricTestRunner::class)
////@HiltAndroidTest
////@RunWith(AndroidJUnit4::class)
//class GeneratorInstrumentedTest {
//    private lateinit var quizQuestionGenerator: QuizQuestionGenerator
//
//    @Before
//    fun setUp() {
//        quizQuestionGenerator = QuizQuestionGenerator()
//    }
//
//    @Test
//    fun emptyHistory() {
//        val history = emptyList<StructureAnswerRecord>()
//        val availableQuestions = 5
//        val questionNumber = 1_000_000
//
//        val generated = quizQuestionGenerator.generateQuestions(
//            structureAnswerHistory = history,
//            availableStructureNumber = availableQuestions,
//            z = questionNumber
//        )
//    }
//}