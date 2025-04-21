package com.vervyle.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vervyle.data.repository.QuizRepository
import com.vervyle.model.Plane
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TestQuizRepository {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var quizRepository: QuizRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun validateTest() = runBlocking {
        val quizData = quizRepository.getQuizByName("dataset_brain_new").first()
        Plane.entries.forEach {
            assert(quizData.annotatedImages.contains(it))
        }
        Plane.entries.forEach { plane ->
            assert(
                quizData.annotatedImages[plane]!!.all { image ->
                    image.annotations.all { it.name.split('-')[0] == plane.name.uppercase() }
                }
            )
        }
        Plane.entries.forEach { plane ->
            quizData.annotatedImages[plane]!!.forEachIndexed { index, image ->
                assert(
                    image.annotations.all { it.name.split('-')[1].toInt() == index + 1 }
                )
            }
        }
    }
}