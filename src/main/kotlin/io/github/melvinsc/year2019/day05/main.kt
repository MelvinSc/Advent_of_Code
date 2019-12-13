package io.github.melvinsc.year2019.day05

import io.github.melvinsc.utils.day.Day
import io.github.melvinsc.utils.linkedListOf
import io.github.melvinsc.year2019.IntCode

fun main() = Day.setMain(Day05)

object Day05 : Day() {
    override fun first(input: String): Int {
        val program = input.split(",").map { it.toInt() }.toIntArray()
        val output = IntCode.eval(program, linkedListOf(1))

        return output.last()
    }

    override fun second(input: String): Int {
        val program = input.split(",").map { it.toInt() }.toIntArray()
        val output = IntCode.eval(program, linkedListOf(5))

        return output.last()
    }
}