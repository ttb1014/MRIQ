package com.vervyle.demo

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test() = runBlocking {
        val ceh = CoroutineExceptionHandler { _, exc ->
            println("Handled $exc")
        }
        try {
            withContext(ceh) {
                val job1: Job = launch() {
                    println("job1 started")
                    launch {
                        println("child1 started")
                        delay(1000L)
                        println("child1 finished")
                    }
                    delay(100L)
                    throw Exception("exception")
                    println("job1 finished")
                }

                val job2 = launch {
                    println("job2 started")
                    launch {
                        println("child2 started")
                        delay(1000L)
                        println("child2 finished")
                    }
                    delay(1000L)
                    println("job2 finished")
                }
                job2.join()
            }
        } catch (e: Exception) {
            println("Caught $e")
        }
    }

    @Test
    fun test2() = runBlocking {
        val channel = Channel<Int>(UNLIMITED)
        val child = launch(Dispatchers.Default) {
            println("child exec from ${Thread.currentThread().name}")
            var i = 0
            while (isActive)
                channel.send(i++)
            println("exec finished")
        }
        println("parent exec from ${Thread.currentThread().name}")
        for (x in channel) {
            println(x)
            if (x == 100_000) {
                child.cancel()
                break
            }
        }
        println("Done")
    }
}