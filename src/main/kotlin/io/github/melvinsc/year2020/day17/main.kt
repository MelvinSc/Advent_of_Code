package io.github.melvinsc.year2020.day17

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day17)

typealias Coordinate = List<Int>
typealias Grid = HashSet<Coordinate>

object Day17 : Day() {

    private fun convert(inputData: String, dimensions: Int): Grid =
        inputData.split("\n").mapIndexed { y, line ->
            line.mapIndexedNotNull { x, char ->
                if (char == '#') {
                    List(dimensions) { i ->
                        when (i) {
                            0 -> x
                            1 -> y
                            else -> 0
                        }
                    }
                } else {
                    null
                }
            }
        }.flatten().toHashSet()

    override fun first(inputData: String): Int {
        var grid = convert(inputData, 3)

        repeat(6) {
            grid = tick(grid)
        }

        return grid.size
    }

    override fun second(inputData: String): Int {
        var grid = convert(inputData, 4)

        repeat(6) {
            grid = tick(grid)
        }

        return grid.size
    }

    private fun tick(grid: Grid): Grid {
        val neighbourInfo = HashMap<Coordinate, Pair<Boolean, Int>>()

        for (cell in grid) {
            for (neighbour in getAllAdjacent(cell)) {
                neighbourInfo[neighbour] = if (neighbourInfo.containsKey(neighbour)) {
                    val old = neighbourInfo[neighbour]!!

                    if (cell == neighbour) {
                        Pair(true, old.second)
                    } else {
                        Pair(old.first, old.second + 1)
                    }
                } else {
                    if (cell == neighbour) {
                        Pair(true, 0)
                    } else {
                        Pair(false, 1)
                    }
                }
            }
        }

        return neighbourInfo.filterValues { (active, neighbourCount) ->
            if (active) {
                neighbourCount in 2..3
            } else {
                neighbourCount == 3
            }
        }.keys.toHashSet()
    }

    private fun getAllAdjacent(cell: Coordinate): Sequence<Coordinate> = sequence {
        fun getAllAdjacentRecur(cell: Coordinate, depth: Int): Sequence<Coordinate> = sequence {
            if (depth < 0) {
                yield(cell)
            } else {
                val minus = cell.toMutableList()
                minus[depth] = minus[depth] - 1
                val plus = cell.toMutableList()
                plus[depth] = plus[depth] + 1

                yieldAll(getAllAdjacentRecur(minus, depth - 1))
                yieldAll(getAllAdjacentRecur(cell, depth - 1))
                yieldAll(getAllAdjacentRecur(plus, depth - 1))
            }
        }

        yieldAll(getAllAdjacentRecur(cell, cell.size - 1))
    }
}