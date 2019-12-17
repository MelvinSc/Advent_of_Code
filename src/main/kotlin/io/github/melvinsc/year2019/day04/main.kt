package io.github.melvinsc.year2019.day04

import io.github.melvinsc.utils.day.Day
import io.github.melvinsc.utils.getGroups

fun main() = Day.setMain(Day04)

object Day04 : Day() {
    override fun first(inputData: String): Int {
        val (from, to) = inputData.split('-').map { it.toInt() }

        return IntRange(from, to)
            .asSequence()
            .map { it.toString() }
            .filter { value -> value.windowed(2).any { it[0] == it[1] } }
            .filter { value -> value.windowed(2).none { it[0] > it[1] } }
            .count()
    }

    override fun second(inputData: String): Int {
        val (from, to) = inputData.split('-').map { it.toInt() }

        return IntRange(from, to)
            .asSequence()
            .map { it.toString() }
            .filter { value -> value.windowed(2).any { it[0] == it[1] } }
            .filter { value -> value.windowed(2).none { it[0] > it[1] } }
            .filter { value ->
                value.toList().getGroups().any {
                    it.size == 2
                }
            }
            .count()
    }
}