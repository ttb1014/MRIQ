package com.vervyle.quiz

import com.vervyle.model.StructureAnswerRecord
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtils
import org.jfree.chart.JFreeChart
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import responseHistoryCase1
import responseHistoryCase2
import responseHistoryCase3
import java.io.File
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.time.Duration
import org.jfree.data.category.DefaultCategoryDataset

class GeneratorUnitTest {
    private lateinit var quizQuestionGenerator: QuizQuestionGenerator

    private val availableStructuresNumber = 89

    @Before
    fun setUp() {
        quizQuestionGenerator = QuizQuestionGenerator()
    }

    fun assertFitsInBounds(min: Int, max: Int, array: IntArray) {
        array.forEach {
            assertTrue(it in min..max)
        }
    }

    fun Map<Int, Int>.generateChart(chartName: String, xLabel: String, yLabel: String): JFreeChart {
        val series = XYSeries("Data").apply {
            this@generateChart.entries.sortedBy { it.key }.forEach { (x, y) ->
                add(x.toDouble(), y.toDouble())
            }
        }

        val dataset = XYSeriesCollection(series)

        return ChartFactory.createXYLineChart(
            chartName,
            xLabel,
            yLabel,
            dataset
        )
    }

    fun List<Double>.generateChart(chartName: String, xLabel: String, yLabel: String): JFreeChart {
        val series = XYSeries(chartName).apply {
            this@generateChart.forEachIndexed { index, value ->
                add(index.toDouble(), value)
            }
        }
        val dataset = XYSeriesCollection(series)

        return ChartFactory.createXYLineChart(
            chartName,
            xLabel,
            yLabel,
            dataset
        )
    }

    fun List<Double>.generateBarChart(chartName: String, xLabel: String, yLabel: String): JFreeChart {
        val dataset = DefaultCategoryDataset().apply {
            this@generateBarChart.forEachIndexed { index, value ->
                addValue(value, chartName, index.toString())
            }
        }

        return ChartFactory.createBarChart(
            chartName,  // chart title
            xLabel,     // x-axis label
            yLabel,     // y-axis label
            dataset     // data
        )
    }

    fun JFreeChart.saveAsPng(fileName: String) {
        val outputFile = File("build/test-results/${fileName}")
        ChartUtils.saveChartAsPNG(outputFile, this, 800, 600)
        assert(outputFile.exists())
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

        val chart = indexToCountMap.generateChart(
            chartName =
            "Тест с пустой историей\n" +
                    "Доступное кол-во вопросов = ${availableQuestions}\n" +
                    "Сгенерировано вопросов = ${questionNumber}",
            xLabel = "Номер вопроса",
            yLabel = "Сгенерировано"
        )
        chart.saveAsPng("EmptyHistoryTest.png")

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

        val chart = probability.generateChart(
            chartName = "Распределение вероятностей\n" +
                    "Доступное кол-во вопросов = ${availableQuestions}",
            xLabel = "Номер вопроса",
            yLabel = "Вероятность выбора вопроса"
        )
        chart.saveAsPng("wrongAnswersAppearOften.png")

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
        val chart = probability.generateChart(
            chartName = "Распределение вероятностей\n" +
                    "Доступное кол-во вопросов = ${availableQuestions}",
            xLabel = "Номер вопроса",
            yLabel = "Вероятность выбора вопроса"
        )
        chart.saveAsPng("oldAnswersAffectLess.png")
        val chart2 = cumulativeProbability.generateChart(
            chartName = "Кумулятивное распределение вероятностей\n" +
                    "Доступное кол-во вопросов = ${availableQuestions}",
            xLabel = "Номер вопроса",
            yLabel = "Кумулятивная вероятность выбора вопроса"
        )
        chart2.saveAsPng("oldAnswersAffectLessCumulative.png")

        assertTrue(probability[1] > probability[0])
        println(probability[1] - probability[0])
    }

    @Test
    fun case1() {
        val history = responseHistoryCase1.map {
            StructureAnswerRecord(
                structureId = it.first - 1,
                isCorrect = it.second != 0,
                timeStamp = it.third
            )
        }

        val availableQuestions = 89

        val cumulativeProbability = quizQuestionGenerator.generateCumulativeProbability(
            structureAnswerHistory = history,
            availableStructureNumber = availableQuestions,
        )

        val probability = cumulativeProbability
            .toMutableList()
            .apply { addFirst(0.0) }
            .zipWithNext { a, b -> b - a }
        val chart = probability.generateChart(
            chartName = "Probability distribution\n" +
                    "Available questions = ${availableQuestions}",
            xLabel = "Question",
            yLabel = "Probability"
        )
        chart.saveAsPng("case1.png")
    }

    @Test
    fun case2() {
        val history = responseHistoryCase2.map {
            StructureAnswerRecord(
                structureId = it.first - 1,
                isCorrect = it.second != 0,
                timeStamp = it.third
            )
        }

        val availableQuestions = 89

        val cumulativeProbability = quizQuestionGenerator.generateCumulativeProbability(
            structureAnswerHistory = history,
            availableStructureNumber = availableQuestions,
        )

        val probability = cumulativeProbability
            .toMutableList()
            .apply { addFirst(0.0) }
            .zipWithNext { a, b -> b - a }
        val chart = probability.generateChart(
            chartName = "Probability distribution\n" +
                    "Available questions = ${availableQuestions}",
            xLabel = "Question",
            yLabel = "Probability"
        )
        chart.saveAsPng("case2.png")
    }

    @Test
    fun case3() {
        val random = Random(0)
        val history = (1..1000).map {
            StructureAnswerRecord(
                structureId = random.nextInt(5),
                isCorrect = false,
                timeStamp = Instant.parse("2025-05-21T12:00:00Z")
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
        val chart = probability.generateChart(
            chartName = "Распределение вероятностей\n" +
                    "Доступное кол-во вопросов = ${availableQuestions}",
            xLabel = "Номер вопроса",
            yLabel = "Вероятность выбора вопроса"
        )
        chart.saveAsPng("case3.png")

    }

    @Test
    fun case4() {
        val history = responseHistoryCase3.map { it ->
            StructureAnswerRecord(
                structureId = it.first - 1,
                isCorrect = it.second != 0,
                timeStamp = it.third
            )
        }

        val availableQuestions = 89

        val cumulativeProbability = quizQuestionGenerator.generateCumulativeProbability(
            structureAnswerHistory = history,
            availableStructureNumber = availableQuestions,
        )

        val probability = cumulativeProbability
            .toMutableList()
            .apply { addFirst(0.0) }
            .zipWithNext { a, b -> b - a }
        println(probability.joinToString(" "))

        val chart = probability.generateBarChart(
            chartName = "Распределение вероятностей\n" +
                    "Доступное кол-во вопросов = ${availableQuestions}",
            xLabel = "Номер вопроса",
            yLabel = "Вероятность выбора вопроса"
        )
        chart.saveAsPng("case4.png")
    }
}