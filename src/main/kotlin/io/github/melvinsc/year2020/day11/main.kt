package io.github.melvinsc.year2020.day11

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day11)

object Day11 : Day() {
    private fun convert(inputData: String) = inputData.lines().map { it.map { it }.toCharArray() }.toTypedArray()

    override fun first(inputData: String): Int {
        var last = convert(inputData)

        var current = getNextTickSimple(last)
        while (!isSame(last, current)) {
            last = current
            current = getNextTickSimple(last)
        }

        return current.sumBy { it.count { c -> c == '#' } }
    }

    override fun second(inputData: String): Int {
        var last = convert(inputData)

        var current = getNextTickAdvanced(last)
        while (!isSame(last, current)) {
            last = current
            current = getNextTickAdvanced(last)
        }

        return current.sumBy { it.count { c -> c == '#' } }
    }

    private fun isSame(first: Array<CharArray>, second: Array<CharArray>): Boolean {
        for (i in first.indices) {
            for (j in first[i].indices) {
                if (first[i][j] != second[i][j]) {
                    return false
                }
            }
        }

        return true
    }

    private fun getNextTickAdvanced(map: Array<CharArray>): Array<CharArray> {
        val ret = map.map { it.copyOf() }.toTypedArray()

        for (i in map.indices) {
            for (j in map[i].indices) {
                if (map[i][j] == 'L') {
                    if (getVisibleTakenCount(map, j, i) == 0) {
                        ret[i][j] = '#'
                    }
                } else if (map[i][j] == '#') {
                    if (getVisibleTakenCount(map, j, i) >= 5) {
                        ret[i][j] = 'L'
                    }
                }
            }
        }

        return ret
    }

    private fun getNextTickSimple(map: Array<CharArray>): Array<CharArray> {
        val ret = map.map { it.copyOf() }.toTypedArray()

        for (i in map.indices) {
            for (j in map[i].indices) {
                if (map[i][j] == 'L') {
                    if (getAdjacentTakenCount(map, i, j) == 0) {
                        ret[i][j] = '#'
                    }
                } else if (map[i][j] == '#') {
                    if (getAdjacentTakenCount(map, i, j) >= 4) {
                        ret[i][j] = 'L'
                    }
                }
            }
        }

        return ret
    }

    private fun getVisibleTakenCount(map: Array<CharArray>, x: Int, y: Int): Int {
        var count = 0

        for (i in -1..+1) {
            for (j in -1..+1) {
                if (i == 0 && j == 0) {
                    continue
                } else {
                    try {
                        var dist = 1
                        while (true) {
                            if (map[y + i * dist][x + j * dist] == '#') {
                                ++count
                                break
                            } else if (map[y + i * dist][x + j * dist] == 'L') {
                                break
                            }
                            ++dist
                        }
                    } catch (ignored: java.lang.IndexOutOfBoundsException) {
                    }
                }
            }
        }

        return count
    }

    private fun getAdjacentTakenCount(map: Array<CharArray>, x: Int, y: Int): Int {
        var count = 0

        for (i in x - 1..x + 1) {
            for (j in y - 1..y + 1) {
                if (i == x && j == y) {
                    continue
                } else {
                    try {
                        if (map[i][j] == '#') {
                            ++count
                        }
                    } catch (ignored: IndexOutOfBoundsException) {
                    }
                }
            }
        }

        return count
    }
}