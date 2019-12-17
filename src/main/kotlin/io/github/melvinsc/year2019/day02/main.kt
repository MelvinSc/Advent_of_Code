package io.github.melvinsc.year2019.day02

import io.github.melvinsc.utils.day.Day
import io.github.melvinsc.year2019.IntCode

fun main() = Day.setMain(Day02)

object Day02 : Day() {
    override fun first(inputData: String): Int {
        val program = inputData.split(",").map { it.toInt() }.toIntArray()
        program[1] = 12
        program[2] = 2
        IntCode().eval(program)
        return program[0]
    }

    override fun second(inputData: String): Int {
        val program = inputData.split(",").map { it.toInt() }.toIntArray()
        for (noun in 0..99) {
            for (verb in 0..99) {
                try {
                    val current = program.copyOf()
                    current[1] = noun
                    current[2] = verb

                    IntCode().eval(current)

                    if (current[0] == 19690720) {
                        return noun * 100 + verb
                    }
                } catch (ignored: IllegalArgumentException) {
                }
            }
        }

        throw NoSuchElementException("No solution was found")
    }
}

