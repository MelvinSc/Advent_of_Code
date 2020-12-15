package io.github.melvinsc.year2020.day03

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day03)

object Day03 : Day() {
    private fun convert(inputData: String) =
        inputData.lines().map { line -> line.map { char -> char == '#' || char == 'X' } }

    override fun first(inputData: String) = getTreeCount(convert(inputData), Pair(3, 1))

    override fun second(inputData: String) =
        listOf(Pair(1, 1), Pair(3, 1), Pair(5, 1), Pair(7, 1), Pair(1, 2)).fold(1L) { product, next ->
            product * getTreeCount(convert(inputData), next)
        }

    private fun getTreeCount(map: List<List<Boolean>>, direction: Pair<Int, Int>): Long {
        var count = 0L

        for (i in 0 until map.size / direction.second) {
            if (map[i * direction.second][(i * direction.first).rem(map[0].size)]) {
                count++
            }
        }

        return count
    }
}