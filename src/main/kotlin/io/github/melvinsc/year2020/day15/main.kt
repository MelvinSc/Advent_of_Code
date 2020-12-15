package io.github.melvinsc.year2020.day15

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day15)

object Day15 : Day() {

    private fun convertFast(inputData: String) =
        inputData.split(',').mapIndexed { index, number -> Pair(number.toInt(), index) }.toMap().toMutableMap()

    override fun first(inputData: String) = getNthNumberFast(convertFast(inputData), 2020)

    override fun second(inputData: String) = getNthNumberFast(convertFast(inputData), 30000000)

    private fun getNthNumberFast(lastIndices: MutableMap<Int, Int>, n: Int): Int {
        var maxIndex = -1
        var currentNumber = -1
        for (element in lastIndices.entries) {
            if (element.value > maxIndex) {
                maxIndex = element.value
                currentNumber = element.key
            }
        }

        lastIndices.remove(currentNumber)

        for (i in lastIndices.size..n - 2) {
            val lastIndex = lastIndices[currentNumber]
            lastIndices[currentNumber] = i
            currentNumber = (i - lastIndex) ?: 0
        }

        return currentNumber
    }

    operator fun Int?.minus(other: Int?) = if (this != null && other != null) this - other else null
}