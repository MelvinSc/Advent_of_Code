package io.github.melvinsc.year2020.day24

import io.github.melvinsc.utils.Vector2D
import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day24)

object Day24 : Day() {

    private fun convert(inputData: String): Set<List<String>> {
        val pattern = """e|w|ne|nw|se|sw""".toRegex()

        return inputData.lines().map { line ->
            pattern.findAll(line).map { it.value }.toList()
        }.toSet()
    }

    override fun first(inputData: String) = getInitialBlackTiles(convert(input)).size

    private fun getInitialBlackTiles(coordinates: Set<List<String>>): Set<Vector2D> {
        val ret = HashSet<Vector2D>()

        for (coordinate in coordinates) {
            val vector = coordinate.fold(Vector2D()) { acc, direction ->
                when (direction) {
                    "nw" -> acc.y++
                    "w" -> {
                        acc.x--
                        acc.y++
                    }
                    "sw" -> acc.x--
                    "se" -> acc.y--
                    "e" -> {
                        acc.x++
                        acc.y--
                    }
                    "ne" -> acc.x++
                }
                acc
            }

            if (ret.contains(vector)) {
                ret.remove(vector)
            } else {
                ret.add(vector)
            }
        }

        return ret
    }

    override fun second(inputData: String): Int {
        var blackTiles = getInitialBlackTiles(convert(inputData))

        repeat(100) {
            blackTiles = getNextTick(blackTiles)
        }

        return blackTiles.size
    }

    private fun getNextTick(blackTiles: Set<Vector2D>): Set<Vector2D> {
        val ret = HashSet<Vector2D>()

        for (tile in getAdjacentTileMap(blackTiles)) {
            if (tile.value.first) {
                if (tile.value.second in 1..2) {
                    ret.add(tile.key)
                }
            } else {
                if (tile.value.second == 2) {
                    ret.add(tile.key)
                }
            }
        }

        return ret
    }

    private fun getAdjacentTileMap(blackTiles: Set<Vector2D>): Map<Vector2D, Pair<Boolean, Int>> {
        fun adjacentCoordinates(coordinate: Vector2D) = setOf(
            Vector2D(coordinate.x, coordinate.y + 1),
            Vector2D(coordinate.x - 1, coordinate.y + 1),
            Vector2D(coordinate.x - 1, coordinate.y),
            Vector2D(coordinate.x, coordinate.y - 1),
            Vector2D(coordinate.x + 1, coordinate.y - 1),
            Vector2D(coordinate.x + 1, coordinate.y)
        )

        val map = HashMap<Vector2D, Pair<Boolean, Int>>()

        for (tile in blackTiles) {
            for (coordinate in adjacentCoordinates(tile)) {
                map[coordinate] = if (map.containsKey(coordinate)) {
                    val info = map[coordinate]!!
                    Pair(info.first, info.second + 1)
                } else {
                    Pair(false, 1)
                }
            }

            map[tile] = if (map.containsKey(tile)) {
                Pair(true, map[tile]!!.second)
            } else {
                Pair(true, 0)
            }
        }

        return map
    }
}