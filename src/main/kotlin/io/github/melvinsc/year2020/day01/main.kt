package io.github.melvinsc.year2020.day01

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day01)

object Day01 : Day() {

    private fun convert(inputData: String) = inputData.lines().map { it.toInt() }

    override fun first(inputData: String): Int {
        val data = convert(inputData)

        for (i in data.indices) {
            for (j in i + 1 until data.size) {
                if (data[i] + data[j] == 2020) {
                    return data[i] * data[j]
                }
            }
        }

        throw IllegalArgumentException("No suitable elements found")
    }

    override fun second(inputData: String): Int {
        val data = convert(inputData)

        for (i in data.indices) {
            for (j in i + 1 until data.size) {
                for (k in j + 1 until data.size) {
                    if (data[i] + data[j] + data[k] == 2020) {
                        return data[i] * data[j] * data[k]
                    }
                }

            }
        }

        throw IllegalArgumentException("No suitable elements found")
    }
}