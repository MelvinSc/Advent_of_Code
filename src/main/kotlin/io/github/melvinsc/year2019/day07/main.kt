package io.github.melvinsc.year2019.day07

import com.marcinmoskala.math.permutations
import io.github.melvinsc.utils.day.Day
import io.github.melvinsc.year2019.IntCode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = Day.setMain(Day07)

object Day07 : Day() {
    override fun first(inputData: String): Long {
        val program = inputData.split(",").map { it.toLong() }.toLongArray()

        return setOf(0L, 1L, 2L, 3L, 4L).permutations()
            .map { permutation ->
                var arg = 0L
                for (i in permutation.indices) {
                    val input = Channel<Long>(Channel.UNLIMITED).apply {
                        runBlocking {
                            launch {
                                send(permutation[i])
                                send(arg)
                            }
                        }
                    }
                    val output = Channel<Long>(Channel.UNLIMITED)
                    IntCode().eval(program.copyOf(), input, output)
                    arg = output.poll() ?: throw IllegalStateException("No output was given")
                }
                arg
            }.max() ?: throw IllegalStateException("No max found")
    }

    override fun second(inputData: String): Long {
        val program = inputData.split(",").map { it.toLong() }.toLongArray()

        return setOf(5L, 6L, 7L, 8L, 9L).permutations()
            .map { permutation ->
                val queues = Array<Channel<Long>>(permutation.size) { Channel(Channel.UNLIMITED) }
                queues.forEachIndexed { index, channel -> runBlocking { launch { channel.send(permutation[index]) } } }

                runBlocking {
                    queues[0].send(0L)
                    for (i: Int in permutation.indices) {
                        launch {
                            IntCode().evalSuspendable(
                                program.copyOf(),
                                queues[i],
                                queues[(i + 1) % permutation.size]
                            )
                        }
                    }
                }

                return@map queues[0].poll() ?: throw IllegalStateException("No output was given")
            }.max() ?: throw IllegalStateException("No max found")
    }
}