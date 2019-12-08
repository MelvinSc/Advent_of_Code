package io.github.melvinsc.year2019.day01

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day01)

object Day01 : Day() {
    override fun first(input: String): Int {
        val intArray = input.split("\n").map { it.toInt() }.toTypedArray()
        var sum = 0

        intArray.forEach { sum += getFuelCost(it) }

        return sum
    }

    override fun second(input: String): Int {
        fun getFuelCostRecursive(weight: Int): Int {
            val ret = getFuelCost(weight)

            return if (ret <= 0) {
                0
            } else {
                ret + getFuelCostRecursive(ret)
            }
        }

        val intArray = input.split("\n").map { it.toInt() }.toTypedArray()
        var sum = 0

        intArray.forEach { weight -> sum += getFuelCostRecursive(weight) }

        return sum
    }

    private fun getFuelCost(weight: Int): Int {
        return weight / 3 - 2
    }
}

