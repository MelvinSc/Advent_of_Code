package io.github.melvinsc.year2020.day02

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day02)

object Day02 : Day() {
    private fun convert(inputData: String) = inputData.lines().map { line ->
        val elements = line.split(" ")
        val count = elements[0].split("-").map { it.toInt() }
        val char = elements[1][0]
        val password = elements[2]
        Triple(count, char, password)
    }

    override fun first(inputData: String) = convert(inputData).count { (indices, char, password) ->
        val count = password.count { c -> c == char }
        indices[0] <= count && count <= indices[1]
    }

    override fun second(inputData: String) = convert(inputData).count { (indices, char, password) ->
        (password[indices[0] - 1] == char) xor (password[indices[1] - 1] == char)
    }
}