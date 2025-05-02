package com.vervyle.quiz

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.junit.Test
import kotlin.random.Random

class QuizQuestionGeneratorTest {

    private lateinit var questionGenerator: QuizQuestionGenerator

    companion object {
        private val random = Random(120301230120L)
        fun generateRandomDate(startYear: Int, endYear: Int): kotlinx.datetime.LocalDate {
            val randomYear = random.nextInt(startYear, endYear + 1)
            val randomMonth = random.nextInt(1, 5)
            val daysInMonth = when (randomMonth) {
                1, 3, 5, 7, 8, 10, 12 -> 31
                4, 6, 9, 11 -> 30
                2 -> if (randomYear % 4 == 0 && (randomYear % 100 != 0 || randomYear % 400 == 0)) 29 else 28
                else -> 30
            }
            val randomDay = random.nextInt(1, daysInMonth + 1)
            return LocalDate(randomYear, randomMonth, randomDay)
        }


        private fun parseDateToKtxInstant(
            dateString: String,
            timeZone: TimeZone = TimeZone.currentSystemDefault()
        ): Instant {
            val instant = Instant.parse(dateString)
            return instant
        }
    }

    init {
        val history = mutableListOf<StructureAnswerRecord>()
        val availableStructures = 10
        repeat(100) {
            val timeStamp =
                generateRandomDate(2025, 2025).atStartOfDayIn(TimeZone.currentSystemDefault())
            val isCorrect = Random.nextBoolean()
            val structureId = Random.nextInt(0, availableStructures)
            history.add(
                StructureAnswerRecord(
                    structureId,
                    isCorrect,
                    timeStamp
                )
            )
        }

        questionGenerator = QuizQuestionGenerator(
            history,
            availableStructures
        )
    }

    @Test
    fun testForgettingRate() {
        val generated = questionGenerator.generateQuestions(5000)
        println(generated.map { it.toString() })
    }
}