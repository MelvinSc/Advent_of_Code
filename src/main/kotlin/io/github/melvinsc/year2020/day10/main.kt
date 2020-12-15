package io.github.melvinsc.year2020.day10

import io.github.melvinsc.utils.day.Day
import kotlin.math.min

fun main() = Day.setMain(Day10)

object Day10 : Day() {

    private fun convert(inputData: String) = inputData.lines().map { it.toInt() }.sorted().toMutableList()

    override fun first(inputData: String): Int {
        val joltAdapter = convert(inputData)

        joltAdapter.add(0, 0)
        joltAdapter.add(joltAdapter.last() + 3)
        var diffOne = 0
        var diffThree = 0
        for (i in 0..joltAdapter.size - 2) {
            when (joltAdapter[i + 1] - joltAdapter[i]) {
                1 -> diffOne++
                3 -> diffThree++
            }
        }

        return diffOne * diffThree
    }

    override fun second(inputData: String): Long {
        val joltAdapter = convert(inputData)

        joltAdapter.add(0, 0)
        joltAdapter.add(joltAdapter.last() + 3)

        val options = LongArray(joltAdapter.size)
        options[options.size - 2] = 1

        for (i in joltAdapter.size - 3 downTo 0) {
            var currentOptions = 0L
            for (j in i until min(i + 4, joltAdapter.size)) {
                if (joltAdapter[j] - joltAdapter[i] <= 3) {
                    ++currentOptions
                    currentOptions += options[j] - 1
                }
            }
            options[i] = currentOptions
        }

        return options.first()
    }
}