package com.vervyle.data.repository

import com.vervyle.mock.UiResourceProvider
import com.vervyle.model.QuizScreenResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeQuizRepository @Inject constructor(
    private val resourceProvider: UiResourceProvider
) : QuizRepository {

    private val cache = mutableMapOf<String, QuizScreenResource>()

    override fun getQuizByName(datasetName: String): Flow<QuizScreenResource> = flow {
        cache[datasetName]?.run {
            emit(this@run)
            return@flow
        }
        val resource = resourceProvider.providesQuizScreenResource()
        cache[datasetName] = resource
        emit(resource)
    }
}