package io.github.melvinsc.year2019.day05

import io.github.melvinsc.utils.day.Day
import io.github.melvinsc.year2019.IntCode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = Day.setMain(Day05)

object Day05 : Day() {
    override fun first(inputData: String): Int =
        solveTask(inputData, 1).poll() ?: throw IllegalStateException("No output was given")

    override fun second(inputData: String): Int =
        solveTask(inputData, 5).poll() ?: throw IllegalStateException("No output was given")

    private fun solveTask(inputData: String, programInput: Int): Channel<Int> {
        val program = inputData.split(",").map { it.toInt() }.toIntArray()
        val input = Channel<Int>(Channel.CONFLATED).apply { runBlocking { launch { send(programInput) } } }
        val output = Channel<Int>(Channel.CONFLATED)

        IntCode().eval(program, input, output)

        return output
    }
}