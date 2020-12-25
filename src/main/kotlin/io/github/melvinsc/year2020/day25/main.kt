package io.github.melvinsc.year2020.day25

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day25)

object Day25 : Day() {

    private fun convert(inputData: String) = inputData.lines().map { it.toLong() }

    override fun first(inputData: String): Long {
        val (cardPk, doorPk) = convert(inputData)

        return transform(findLoopSize(cardPk), doorPk)
    }

    override fun second(inputData: String) = "Pay the room"

    private fun findLoopSize(target: Long): Int =
        generateSequence(1L) { step(it) }.indexOf(target)

    private fun transform(loopSize: Int, subjectNumber: Long): Long =
        generateSequence(1L) { step(it, subjectNumber) }.drop(loopSize).first()

    private fun step(value: Long, subjectNumber: Long = 7) = (subjectNumber * value) % 20201227
}