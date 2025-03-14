package com.vervyle.demo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Test
import kotlin.random.Random

class CoroutineTest {
    data class Shape(val location: Location, val data: ShapeData)
    data class Location(val x: Int, val y: Int)
    class ShapeData

    class ShapeCollector(private val workerCount: Int) {

        fun CoroutineScope.start(
            locations: ReceiveChannel<Location>,
            shapesOutput: SendChannel<Shape>
        ) = launch(Dispatchers.IO) {
            val locationsToProcess = Channel<Location>()
            val locationsProcessed = Channel<Location>(capacity = 1)

            repeat(workerCount) {
                worker(locationsToProcess, locationsProcessed, shapesOutput)
            }
            collectShapes(locations, locationsToProcess, locationsProcessed)
        }

        private fun CoroutineScope.worker(
            locationsToProcess: ReceiveChannel<Location>,
            locationsProcessed: SendChannel<Location>,
            shapesOutput: SendChannel<Shape>
        ) = launch(Dispatchers.IO) {
            for (loc in locationsToProcess) {
                try {
                    val data = getShapeData(loc)
                    val shape = Shape(loc, data)
                    shapesOutput.send(shape)
                } finally {
                    locationsProcessed.send(loc)
                }
            }
        }

        private suspend fun getShapeData(
            location: Location
        ): ShapeData = withContext(Dispatchers.IO) {
            delay(10L)
            ShapeData()
        }

        private fun CoroutineScope.collectShapes(
            locations: ReceiveChannel<Location>,
            locationsToProcess: SendChannel<Location>,
            locationsProcessed: ReceiveChannel<Location>
        ) = launch(Dispatchers.Default) {
            val locationBeingProcessed = mutableListOf<Location>()

            while (true) {
                select<Unit> {
                    locationsProcessed.onReceive {
                        locationBeingProcessed.remove(it)
                    }
                    locations.onReceive {
                        if (!locationBeingProcessed.any { loc ->
                                loc == it
                            }) {
                            locationBeingProcessed.add(it)

                            locationsToProcess.send(it)
                        }
                    }
                }
            }
        }
    }

    private var count = 0

    private fun CoroutineScope.consumeShapes(
        shapesInput: ReceiveChannel<Shape>
    ) = launch {
        for (shape in shapesInput) {
            count++
        }
    }

    private fun CoroutineScope.sendLocations(
        locationsOutput: SendChannel<Location>
    ) = launch {
        withTimeoutOrNull(6000L) {
            while (true) {
                val location = Location(Random.nextInt(), Random.nextInt())
                locationsOutput.send(location)
            }
        }
        println("Received $count shapes")
    }

    @Test
    fun test3() = runBlocking {
        val shapes = Channel<Shape>()
        val locations = Channel<Location>()

        val collectingJob: Job
        val consumingJob: Job

        with(ShapeCollector(4)) {
            collectingJob = start(locations, shapes)
            consumingJob = consumeShapes(shapes)
        }

        val job = sendLocations(locations)
        job.join()
        collectingJob.cancel()
        consumingJob.cancel()
    }
}