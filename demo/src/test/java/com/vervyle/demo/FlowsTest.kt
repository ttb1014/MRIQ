package com.vervyle.demo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatten
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.whileSelect
import kotlinx.coroutines.withContext
import org.junit.Test

class FlowsTest {

    private fun numbers() = flow {
        emit(1)
        emit(2)
        emit(3)
    }

    private suspend fun tr(i: Int) = withContext(Dispatchers.Default) {
        delay(10L)
        "${i + 12}"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test1() = runBlocking {
        val numbers = numbers().map {
            flow {
                emit(tr(it))
            }
        }.flattenMerge(4)
    }

    @OptIn(ObsoleteCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    fun <T> Flow<T>.bufferTimeout(maxSize: Int, maxDelayMillis: Long): Flow<List<T>> = flow {
        require(maxSize > 0) { "maxSize must be greater than 0" }
        require(maxDelayMillis > 0) { "maxDelayMillis must be greater than 0" }

        coroutineScope {
            val channel = produceIn(this)
            val ticker = ticker(maxDelayMillis)
            val buffer = mutableListOf<T>()

            suspend fun emitBuffer() {
                if (buffer.isNotEmpty()) {
                    emit(buffer)
                    buffer.clear()
                }
            }

            try {
                whileSelect {
                    channel.onReceive { value ->
                        buffer.add(value)
                        if (buffer.size >= maxSize) emitBuffer()
                        true
                    }
                    ticker.onReceive {
                        emitBuffer()
                        true
                    }
                }
            } catch (e: ClosedReceiveChannelException) {
                emitBuffer()
            } finally {
                channel.cancel()
                ticker.cancel()
            }
        }
    }

    @Test
    fun test2() = runBlocking {
        val flow = (1..100).asFlow().onEach { delay(10L) }
        val startTime = System.currentTimeMillis()
        flow.bufferTimeout(4, 100).collect {
            val time = System.currentTimeMillis() - startTime
            println("$time ms : $it")
        }
    }
}