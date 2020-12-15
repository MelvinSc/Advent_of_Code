package io.github.melvinsc.year2020.day09

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day09)

object Day09 : Day() {
    private fun convert(inputData: String) = inputData.lines().map { it.toLong() }

    override fun first(inputData: String): Long {
        val xmas = convert(inputData)

        for (i in xmas.indices) {
            if (!isValid(xmas.subList(i, i + 25), xmas[i + 25])) {
                return xmas[i + 25]
            }
        }

        throw IllegalArgumentException("No flaw found")
    }

    override fun second(inputData: String): Long {
        val target = first(inputData)
        val xmas = convert(inputData)

        for (i in xmas.indices) {
            for (j in i + 1 until xmas.size) {
                val subList = xmas.subList(i, j + 1)
                val sum = subList.sum()
                if (subList.sum() == target) {
                    return subList.minOrNull()!! + subList.maxOrNull()!!
                } else if (sum > target) {
                    break
                }
            }
        }

        throw IllegalArgumentException("No weakness found")
    }

    private fun isValid(candidates: List<Long>, target: Long): Boolean {
        for (i in candidates.indices) {
            for (j in i + 1 until candidates.size) {
                if (candidates[i] + candidates[j] == target) {
                    return true
                }
            }
        }

        return false
    }
}