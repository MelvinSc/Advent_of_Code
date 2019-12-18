package io.github.melvinsc.year2019.day02

import io.github.melvinsc.utils.day.Day
import io.github.melvinsc.year2019.IntCode

fun main() = Day.setMain(Day02)

object Day02 : Day() {
    override fun first(inputData: String): Long {
        val program = inputData.split(",").map { it.toLong() }.toLongArray()
        program[1] = 12L
        program[2] = 2L
        IntCode().eval(program)
        return program[0]
    }

    override fun second(inputData: String): Long {
        val program = inputData.split(",").map { it.toLong() }.toLongArray()
        for (noun in 0L..99L) {
            for (verb in 0L..99L) {
                try {
                    val current = program.copyOf()
                    current[1] = noun
                    current[2] = verb

                    IntCode().eval(current)

                    if (current[0] == 19690720L) {
                        return noun * 100 + verb
                    }
                } catch (ignored: IllegalArgumentException) {
                }
            }
        }

        throw NoSuchElementException("No solution was found")
    }
}

