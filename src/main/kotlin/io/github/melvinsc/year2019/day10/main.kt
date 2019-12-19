package io.github.melvinsc.year2019.day10

import io.github.melvinsc.utils.Vector2D
import io.github.melvinsc.utils.day.Day
import kotlin.math.atan2

fun main() = Day.setMain(Day10)

object Day10 : Day() {
    override fun first(inputData: String): Int {
        val asteroids = inputData.lines()
            .mapIndexed { y, line ->
                line.mapIndexedNotNull { x, c -> if (c == '#') Vector2D(x, y) else null }
            }.flatten()

        return asteroids.map { base ->
            asteroids.filter { it != base }
                .map { base.direction(it) }.distinct().count()
        }.max() ?: throw IllegalStateException("No max was found")
    }

    override fun second(inputData: String): Int {
        val asteroids = inputData.lines()
            .mapIndexed { y, line ->
                line.mapIndexedNotNull { x, c -> if (c == '#') Vector2D(x, y) else null }
            }.flatten()

        val laser = asteroids.maxBy { curr ->
            asteroids.filter { it != curr }
                .map { curr.direction(it) }.distinct().count()
        } ?: throw IllegalStateException("No laser position was found")

        val lines = (asteroids - laser)
            .groupBy { other -> laser.direction(other) }
            .mapValues { (_, line) -> line.sortedBy { (it - laser).lengthSquared() } }
            .map { (dir, list) -> atan2(-dir.y.toDouble(), dir.x.toDouble()) to list }
            .sortedByDescending { it.first }

        val topDir = atan2(1.0, 0.0)

        //  liro = Lines In Rotating Order
        val liro = (lines.dropWhile { it.first > topDir } + lines.takeWhile { it.first > topDir }).map { it.second }

        val maxRotations = liro.map { it.size }.max() ?: throw IllegalStateException("No max length was found")

        val ordered = (0 until maxRotations).flatMap { pos ->
            liro.mapNotNull { line -> line.getOrNull(pos) }
        }

        return ordered[199].let { it.x * 100 + it.y }
    }
}