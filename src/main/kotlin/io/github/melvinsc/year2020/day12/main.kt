package io.github.melvinsc.year2020.day12

import io.github.melvinsc.utils.Vector2D
import io.github.melvinsc.utils.day.Day
import kotlin.math.absoluteValue

fun main() = Day.setMain(Day12)

object Day12 : Day() {
    private fun convert(inputData: String) = inputData.lines().map { line ->
        Pair(line.first(), line.substring(1).toInt())
    }

    override fun first(inputData: String): Int {
        val actions = convert(inputData)

        val pos = Vector2D(0, 0)
        var direction = 90
        for (action in actions) {
            when (action.first) {
                'N' -> pos.y += action.second
                'S' -> pos.y -= action.second
                'E' -> pos.x += action.second
                'W' -> pos.x -= action.second
                'L' -> direction -= action.second
                'R' -> direction += action.second
                'F' -> {
                    when (Math.floorMod(direction, 360)) {
                        0 -> pos.y += action.second
                        180 -> pos.y -= action.second
                        90 -> pos.x += action.second
                        270 -> pos.x -= action.second
                    }
                }
                else -> throw IllegalArgumentException("Action not supported: $action")
            }
        }

        return pos.x.absoluteValue + pos.y.absoluteValue
    }

    override fun second(inputData: String): Int {
        val actions = convert(inputData)

        var shipPos = Vector2D(0, 0)
        val wayPos = Vector2D(10, 1)
        for (action in actions) {
            when (action.first) {
                'N' -> wayPos.y += action.second
                'S' -> wayPos.y -= action.second
                'E' -> wayPos.x += action.second
                'W' -> wayPos.x -= action.second
                'L' -> wayPos.rotate(-action.second)
                'R' -> wayPos.rotate(action.second)
                'F' -> shipPos += wayPos * action.second
                else -> throw IllegalArgumentException("Action not supported: $action")
            }
        }

        return shipPos.x.absoluteValue + shipPos.y.absoluteValue
    }
}
