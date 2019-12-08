package io.github.melvinsc.year2019.day03

import io.github.melvinsc.utils.Directions
import io.github.melvinsc.utils.Vector2D
import io.github.melvinsc.utils.day.Day
import kotlin.math.abs

fun main() = Day.setMain(Day03)

object Day03 : Day() {
    override fun first(input: String): Int? {
        val (wire1, wire2) = input
            .lines()
            .map { line -> line.split(',').map { Directions.valueOf(it.take(1)) to it.substring(1).toInt() } }
            .map { wire ->
                wire.fold(listOf(Vector2D())) { acc, (dir, length) ->
                    val start = acc.last()
                    return@fold acc + (1..length).map { start + dir.step * it }
                }
            }
            .map { it.toSet() - Vector2D() }

        return (wire1.intersect(wire2)).map { abs(it.x) + abs(it.y) }.min()
    }

    override fun second(input: String): Int? {
        val (wire1, wire2) = input
            .lines()
            .map { line -> line.split(',').map { Directions.valueOf(it.take(1)) to it.substring(1).toInt() } }
            .map { wire ->
                wire.fold(listOf(Vector2D() to 0)) { acc, (dir, length) ->
                    val start = acc.last()
                    return@fold acc + (1..length).map { start.first + dir.step * it to start.second + it }
                }
            }
            .map { it.drop(1) }
            .map { wire ->
                wire
                    .groupBy({ it.first }, { it.second })
                    .mapValues { (_, points) -> points.min() ?: Int.MAX_VALUE }
            }

        return wire1.mapValues { (point, distance) -> distance + (wire2[point] ?: 1_000_000_000) }.minBy { it.value }
            ?.value
    }
}
