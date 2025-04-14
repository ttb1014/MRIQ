package com.vervyle.demo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test

class CoroutineIntTest {

    @Test
    fun checkCoroutineSwitchContext() = runBlocking(Dispatchers.Default) {
        val scope = coroutineContext + Job()
        val job = launch(scope) {
        }
    }
}