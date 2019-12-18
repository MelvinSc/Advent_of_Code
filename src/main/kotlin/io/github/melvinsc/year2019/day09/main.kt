package io.github.melvinsc.year2019.day09

import io.github.melvinsc.utils.day.Day
import io.github.melvinsc.year2019.IntCode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = Day.setMain(Day09)

object Day09 : Day() {
    override fun first(inputData: String): Long =
        solveTask(inputData, 1).poll() ?: throw IllegalStateException("No output was given")

    override fun second(inputData: String): Long =
        solveTask(inputData, 2).poll() ?: throw IllegalStateException("No output was given")

    private fun solveTask(inputData: String, programInput: Long): Channel<Long> {
        val program = inputData.split(",").map { it.toLong() }.toLongArray()
        val input = Channel<Long>(Channel.UNLIMITED).apply {
            runBlocking {
                launch {
                    send(programInput)
                }
            }
        }
        val output = Channel<Long>(Channel.UNLIMITED)

        IntCode().eval(program, input, output)

        return output
    }
}