package com.vervyle.quiz

import com.vervyle.model.StructureAnswerRecord
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.random.Random
import kotlin.time.DurationUnit

class QuizQuestionGenerator @Inject constructor() {
    private val current = Clock.System.now()
    private val random = Random(RANDOM_SEED)

    fun generateQuestions(
        structureAnswerHistory: List<StructureAnswerRecord>,
        availableStructureNumber: Int,
        z: Int,
    ): IntArray {
        val c = generateCumulativeProbability(
            structureAnswerHistory,
            availableStructureNumber,
        )

        val result = IntArray(z)
        repeat(z) { index ->
            val u = random.nextDouble()
            val questionIndex = c.binarySearch {
                when {
                    it < u -> -1
                    it >= u -> 1
                    else -> 0
                }
            }.inv()
            assert(questionIndex >= 0)
            assert(questionIndex <= availableStructureNumber)
            result[index] = questionIndex
        }
        return result
    }

    fun generateCumulativeProbability(
        structureAnswerHistory: List<StructureAnswerRecord>,
        availableStructureNumber: Int,
    ): List<Double> {
        val stat = generateStructureStatistics(
            structureAnswerHistory,
            availableStructureNumber
        )
        val score = stat
            .map { value ->
                1.0 - value
            }
            .normalize()

        // почему софтмакс всё "выравнивает"?
        val p = score.normalize()

        val c = p.runningFold(0.0) { acc, value ->
            acc + value
        }.drop(1)

        return c
    }

    private fun List<Double>.softmax(): List<Double> {
        val intermediate = this.map { Math.E.pow(it / T) }
        val intermediateSum = intermediate.fold(0.0) { acc, value ->
            acc + value
        }
        return intermediate.map { it / intermediateSum }
    }

    private fun List<Double>.normalize(): List<Double> {
        val sum = this.fold(0.0) { acc, value ->
            acc + value
        }
        return this.map { it / sum }
    }

    private fun generateStructureStatistics(
        structureAnswerHistory: List<StructureAnswerRecord>,
        availableStructureNumber: Int
    ): List<Double> {
        val correctStatistics = Array(availableStructureNumber) { 0.0 }
        val allStatistics = Array(availableStructureNumber) { 0.0 }

        structureAnswerHistory
            .takeLast(ACCOUNT_RECORD_LIMIT)
            .forEach { record ->
                assert(record.structureId < availableStructureNumber)
                allStatistics[record.structureId] += getWeight(record.timeStamp)
                if (record.isCorrect)
                    correctStatistics[record.structureId] += getWeight(record.timeStamp)
            }

        val threshold = allStatistics
            .zip(correctStatistics)
            .filter { it.second == 0.0 }
            .minOfOrNull { it.first }?.div(10) ?: THRESHOLD

        return correctStatistics.zip(allStatistics)
            .map { (correct, all) ->
                if (all == 0.0) {
                    return@map 0.0
                }
                if (correct == 0.0) {
                    return@map (correct + threshold) / all
                }
                correct / all
            }
    }

    private fun getWeight(instant: Instant): Double {
        val power = -1 * LAMBDA * (current - instant).toDouble(DurationUnit.SECONDS)
        val w = Math.E.pow(power)
        return w.absoluteValue
    }


    companion object {
        private const val T = 1
        private const val LAMBDA = 1E-4
        private const val ACCOUNT_RECORD_LIMIT = 1000
        private const val RANDOM_SEED = 12312312312L
        private const val THRESHOLD = 1e-15
    }
}