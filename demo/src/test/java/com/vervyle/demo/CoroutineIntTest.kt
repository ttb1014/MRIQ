package com.vervyle.demo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CoroutineIntTest {

    @Test
    fun checkCoroutineSwitchContext() = runBlocking(Dispatchers.Default) {
        val scope = coroutineContext + Job()
        val job = launch(scope) {
        }
    }
}