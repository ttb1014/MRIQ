package com.vervyle.quiz

import com.vervyle.model.StructureAnswerRecord
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.math.absoluteValue
import kotlin.time.Duration

class GeneratorUnitTest {
    private lateinit var quizQuestionGenerator: QuizQuestionGenerator

    @Before
    fun setUp() {
        quizQuestionGenerator = QuizQuestionGenerator()
    }

    fun assertFitsInBounds(min: Int, max: Int, array: IntArray) {
        array.forEach {
            assertTrue(it in min..max)
        }
    }

    @Test
    fun emptyHistory() {
        val history = emptyList<StructureAnswerRecord>()
        val availableQuestions = 5
        val questionNumber = 1_000_000
        val threshold = 1e-6f

        val generated = quizQuestionGenerator.generateQuestions(
            structureAnswerHistory = history,
            availableStructureNumber = availableQuestions,
            z = questionNumber
        )

        assertFitsInBounds(0, availableQuestions - 1, generated)

        val indexToCountMap = (0..availableQuestions - 1)
            .asIterable()
            .associateWith { 0 }
            .toMutableMap()
        generated.forEach {
            indexToCountMap[it] = indexToCountMap[it]!! + 1
        }

        val averageCount = questionNumber / availableQuestions
        indexToCountMap.values.forEach {
            assertTrue(((it - averageCount) / averageCount).absoluteValue < threshold)
        }
    }

    @Test
    fun wrongAnsweredQuestionsAppearOften() {
        val history = listOf(
            0, 0, 0, 0, 0, 0, 1, 1, 1, 1,
        )
            .map { it != 0 }
            .zip(
                listOf(
                    4, 4, 4, 4, 4, 4, 1, 2, 3, 0,
                )
            ).map {
                StructureAnswerRecord(
                    structureId = it.second,
                    isCorrect = it.first,
                    timeStamp = Clock.System.now()
                )
            }
        val availableQuestions = 5
        val questionNumber = 1_000
        val threshold = 1e-6f

        val cumulativeProbability = quizQuestionGenerator.generateCumulativeProbability(
            structureAnswerHistory = history,
            availableStructureNumber = availableQuestions,
        )

        val probability = cumulativeProbability
            .toMutableList()
            .apply { addFirst(0.0) }
            .zipWithNext { a, b -> b - a }

        val buffer = probability[0]
        val diff = probability.map { it - buffer }.dropLast(1)
        diff.forEach { assertTrue(it < threshold) }
    }

    @Test
    fun oldAnswersAffectLess() {
        val history = listOf(
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1
        ).map { it != 0 }
            .zip(
                listOf(
                    0, 0, 0, 0, 1, 1, 1, 1, 2, 3, 4
                )
            )
            .map {
                StructureAnswerRecord(
                    structureId = it.second,
                    isCorrect = it.first,
                    timeStamp = when (it.second) {
                        0 -> Clock.System.now().minus(Duration.parse("2d"))
                        else -> Clock.System.now()
                    }
                )
            }
        val availableQuestions = 5
        val questionNumber = 1_000
        val threshold = 1e-6f

        val cumulativeProbability = quizQuestionGenerator.generateCumulativeProbability(
            structureAnswerHistory = history,
            availableStructureNumber = availableQuestions,
        )

        val probability = cumulativeProbability
            .toMutableList()
            .apply { addFirst(0.0) }
            .zipWithNext { a, b -> b - a }

        assertTrue(probability[1] > probability[0])
        println(probability[1] - probability[0])
    }
}