package io.github.melvinsc.year2020.day08

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day08)

object Day08 : Day() {
    override fun first(inputData: String): Int {
        val result = IntCode2.execute(input.lines())

        return if (result.first == 1) {
            result.second
        } else {
            throw IllegalArgumentException("IntCode2 didn't terminate like planned")
        }
    }

    override fun second(inputData: String): Int {
        for (permutation in permutations(input.lines())) {
            val result = IntCode2.execute(permutation)
            if (result.first == 0) {
                return result.second
            }
        }

        throw IllegalArgumentException("No fix was found")
    }

    private fun permutations(instructions: List<String>) = sequence {
        val current = instructions.toMutableList()
        for (i in current.indices) {
            if (current[i].startsWith("jmp")) {
                current[i] = "nop" + current[i].drop(3)
                yield(current)
                current[i] = "jmp" + current[i].drop(3)
            } else if (current[i].startsWith("nop") && current[i].split(' ')[1].toInt() != 0) {
                current[i] = "jmp" + current[i].drop(3)
                yield(current)
                current[i] = "nop" + current[i].drop(3)
            }
        }
    }
}