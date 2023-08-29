package io.github.melvinsc.year2015.day01

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day01)

object Day01 : Day() {
    override fun first(inputData: String): Int {
        return mapParenthesesToInts(inputData).sum()
    }
    
    override fun second(inputData: String): Int {
        return mapParenthesesToInts(inputData).reduceIndexed { index: Int, acc: Int, i: Int ->
            if (acc < 0) return index else acc + i
        }
    }

    private fun mapParenthesesToInts(inputData: String) = inputData.map { c -> if (c == '(') 1 else -1 }
}