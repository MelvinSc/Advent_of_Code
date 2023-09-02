package io.github.melvinsc.year2015.day03

import io.github.melvinsc.utils.Directions
import io.github.melvinsc.utils.Vector2D
import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day03)

object Day03 : Day() {
    override fun first(inputData: String): Int {
        val houseLocations = HashSet(listOf(Vector2D()))

        inputData.map(Day03::mapCharToDirection).fold(Vector2D()) { currentPosition, direction ->
            val current = currentPosition + direction.step
            houseLocations.add(current)
            return@fold current
        }

        return houseLocations.size
    }

    override fun second(inputData: String): Int {
        val houseLocations = HashSet(listOf(Vector2D()))

        inputData.map(Day03::mapCharToDirection).foldIndexed(mutableListOf(Vector2D(), Vector2D())) { i, currentPositions, direction ->
            val current = currentPositions[i%2] + direction.step
            houseLocations.add(current)
            currentPositions[i%2] = current
            return@foldIndexed currentPositions
        }

        return houseLocations.size
    }

    private fun mapCharToDirection(dir: Char): Directions = when(dir) {
        '^' -> Directions.U
        'v' -> Directions.D
        '<' -> Directions.L
        '>' -> Directions.R
        else -> throw IllegalArgumentException()
    }
}