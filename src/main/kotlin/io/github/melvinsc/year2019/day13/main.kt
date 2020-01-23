package io.github.melvinsc.year2019.day13

import io.github.melvinsc.utils.Vector2D
import io.github.melvinsc.utils.day.Day
import io.github.melvinsc.year2019.IntCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.sign

fun main() = Day.setMain(Day13)

object Day13 : Day() {
    const val MANUAL = false

    @ExperimentalCoroutinesApi
    override fun first(inputData: String): Int {
        val program = inputData.split(",").map { it.toLong() }.toLongArray()
        val input = Channel<Long>(Channel.UNLIMITED)
        val output = Channel<Long>(Channel.UNLIMITED)

        IntCode().eval(program, input, output)
        val tiles = HashSet<Vector2D>()

        runBlocking {
            launch {
                while (!output.isEmpty) {
                    val coordinate = Vector2D(output.receive().toInt(), output.receive().toInt())
                    if (output.receive() == 2L) {
                        tiles.add(coordinate)
                    }
                }
            }
        }


        return tiles.size
    }

    override fun second(inputData: String): Int {
        val program = inputData.split(",").map { it.toLong() }.toLongArray()
        val input = Channel<Long>(Channel.UNLIMITED)
        val output = Channel<Long>(Channel.UNLIMITED)
        val frameBuffer = HashMap<Vector2D, Int>()
        var frameHasChanged = false
        val scorePosition = Vector2D(-1, 0)
        var paddleX = 0L
        var ballX = 0L
        var score = 0

        program[0] = 2

        runBlocking {
            val intCode = launch {
                IntCode().eval(program, input, output)
            }.apply { start() }

            launch {
                val stringBuilder = StringBuilder()
                var lastY: Int? = null
                while (!intCode.isCompleted) {
                    delay(10)
                    val elements = mutableListOf<Long?>()

                    while (true) {
                        repeat(3) {
                            elements.add(output.poll())
                        }

                        if (elements[2] != null) {
                            frameBuffer[Vector2D(elements[0]!!.toInt(), elements[1]!!.toInt())] = elements[2]!!.toInt()
                            if (elements[2]!! == 3L) {
                                paddleX = elements[0]!!
                            } else if (elements[2]!! == 4L) {
                                ballX = elements[0]!!
                            }
                            elements.clear()
                            frameHasChanged = true
                        } else {
                            break
                        }
                    }

                    if (frameHasChanged) {
                        frameBuffer.toSortedMap(compareBy<Vector2D> { it.y }.thenBy { it.x })
                            .forEach { (position, id) ->
                                if (lastY == null || lastY != position.y) {
                                    stringBuilder.append("\n\t\t")
                                    lastY = position.y
                                }
                                if (position == scorePosition) {
                                    score = id.toInt()
                                    stringBuilder.append(score).append("\n\t\t")
                                } else {
                                    stringBuilder.append(
                                        when (id.toInt()) {
                                            0 -> " "
                                            1 -> "\u2588"
                                            2 -> "\u2592"
                                            3 -> "\u2593"
                                            4 -> "\u25cf"
                                            else -> throw IllegalArgumentException("Illegal id=$id at position ${position.x},${position.y}")
                                        }
                                    )
                                }
                            }
                        print(stringBuilder.append("\n").toString())
                        stringBuilder.clear()
                        frameHasChanged = false

                        if (MANUAL) {
                            while (when (readLine()) {
                                    "a" -> {
                                        input.send(-1)
                                        false
                                    }
                                    "s" -> {
                                        input.send(0)
                                        false
                                    }
                                    "d" -> {
                                        input.send(1)
                                        false
                                    }
                                    else -> true
                                }
                            ) {/*nothing to do here*/
                            }
                        } else {
                            input.send(sign((ballX - paddleX).toDouble()).toLong())
                        }
                    }
                }
                println("finished")
            }
        }
        return score
    }
}