package io.github.melvinsc.year2020.day05

import io.github.melvinsc.utils.day.Day
import io.github.melvinsc.utils.exp

fun main() = Day.setMain(Day05)

object Day05 : Day() {
    private fun convert(inputData: String) = inputData.lines()

    override fun first(inputData: String) = convert(inputData).maxOf { line -> getSeatID(getSeatPosition(line)) }

    override fun second(inputData: String): Int {
        val ids = convert(inputData).map { line -> getSeatID(getSeatPosition(line)) }.sorted()
        return ids.withIndex().first { indexedValue ->
            indexedValue.value - indexedValue.index != ids.first()
        }.value - 1
    }

    private fun getSeatID(position: Pair<Int, Int>) = position.first * 8 + position.second

    private fun getSeatPosition(seat: String) = Pair(getRow(seat.take(7)), getColumn(seat.takeLast(3)))

    private fun getRow(rowStr: String) = rowStr.foldIndexed(0) { index, current, char ->
        return@foldIndexed current + if (char == 'B') {
            exp(2, 6 - index)
        } else {
            0
        }
    }

    private fun getColumn(rowStr: String) = rowStr.foldIndexed(0) { index, current, char ->
        return@foldIndexed current + if (char == 'R') {
            exp(2, 2 - index)
        } else {
            0
        }
    }
}