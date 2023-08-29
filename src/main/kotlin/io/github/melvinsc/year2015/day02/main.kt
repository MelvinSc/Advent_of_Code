package io.github.melvinsc.year2015.day02

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day02)

object Day02 : Day() {
    override fun first(inputData: String): Int {
        return inputData.split('\n')
            .map { line -> line.split('x').map { i -> i.toInt() } }
            .map { (a, b, c) -> listOf(a * b, a * c, b * c) }
            .sumOf { areas -> 2 * areas.sum() + areas.min() }
    }

    override fun second(inputData: String): Int {
        return inputData.split('\n')
            .map { line -> line.split('x').map { i -> i.toInt() } }
            .sumOf { sides -> 2 * sides.sorted().dropLast(1).sum() + sides.reduce { prod, i -> i * prod } }
    }
}