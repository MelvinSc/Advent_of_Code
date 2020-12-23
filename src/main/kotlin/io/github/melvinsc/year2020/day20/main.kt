package io.github.melvinsc.year2020.day20

import io.github.melvinsc.utils.Vector2D
import io.github.melvinsc.utils.day.Day
import io.github.melvinsc.utils.getEveryNth
import io.github.melvinsc.utils.reorientate
import io.github.melvinsc.utils.toBooleanArray
import kotlin.math.absoluteValue

fun main() = Day.setMain(Day20)

object Day20 : Day() {

    private fun convert(inputData: String): Set<Tile> {
        val rawTiles = inputData.split("\n\n").map { it.split(':') }
        val dim = rawTiles.first().last().indexOf('\n', 1) - 1

        return rawTiles.map { rawTileData ->
            assert(rawTileData.size == 2)
            val id = rawTileData.first().substring(5).toInt()
            val rawTile = rawTileData.last().replace("\n", "")

            val inner = Array(dim - 2) { CharArray(dim - 2) }
            for (i in 1..dim - 2) {
                inner[i - 1] = rawTile.substring(i * dim + 1, i * dim + 9).toCharArray()
            }

            val candidates = listOf(
                rawTile.substring(0, dim).toBooleanArray(),                              // top
                rawTile.getEveryNth(dim, dim - 1).toBooleanArray(),                // right
                rawTile.substring(dim * (dim - 1)).toBooleanArray(),            // bottom
                rawTile.getEveryNth(dim).toBooleanArray(),                               // left
                rawTile.substring(0, dim).reversed().toBooleanArray(),                   // reversed top
                rawTile.getEveryNth(dim, dim - 1).reversed().toBooleanArray(),     // reversed right
                rawTile.substring(dim * (dim - 1)).reversed().toBooleanArray(), // reversed bottom
                rawTile.getEveryNth(dim).reversed().toBooleanArray()                     // reversed left
            )

            Tile(
                id, arrayOf(
                    arrayOf(candidates[0], candidates[1], candidates[2], candidates[3]),
                    arrayOf(candidates[1], candidates[6], candidates[3], candidates[4]),
                    arrayOf(candidates[6], candidates[7], candidates[4], candidates[5]),
                    arrayOf(candidates[7], candidates[0], candidates[5], candidates[2]),
                    arrayOf(candidates[4], candidates[3], candidates[6], candidates[1]),
                    arrayOf(candidates[3], candidates[2], candidates[1], candidates[0]),
                    arrayOf(candidates[2], candidates[5], candidates[0], candidates[7]),
                    arrayOf(candidates[5], candidates[4], candidates[7], candidates[6]),
                ), inner
            )
        }.toSet()
    }

    override fun first(inputData: String): Long {
        val tiles = convert(inputData)
        val map = arrange(tiles.toMutableSet())

        val corners = setOf(
            map.maxByOrNull { tile -> tile.key.x + tile.key.y }!!,
            map.maxByOrNull { tile -> tile.key.x - tile.key.y }!!,
            map.maxByOrNull { tile -> -tile.key.x + tile.key.y }!!,
            map.maxByOrNull { tile -> -tile.key.x - tile.key.y }!!
        )

        return corners.fold(1) { product, tile -> product * tile.value.id }
    }

    override fun second(inputData: String): Int {
        val tiles = convert(inputData)
        val map = arrange(tiles.toMutableSet())

        val sortedMap = map.toList().sortedBy { tile -> tile.first.y * map.size + tile.first.x }
        val min = Pair(sortedMap.first().first.y.absoluteValue, sortedMap.first().first.x.absoluteValue)
        sortedMap.forEach { (vector, _) ->
            vector.y += min.first
            vector.x += min.second
        }

        val dimTileX = sortedMap.last().first.x + 1
        val dimTileY = sortedMap.last().first.y + 1
        val dimFrameX = sortedMap.first().second.getInner().size
        val dimFrameY = sortedMap.first().second.getInner().first().size
        val dimGlobalX = dimTileX * dimFrameX
        val dimGlobalY = dimTileY * dimFrameY

        val image = Array(dimGlobalX) { CharArray(dimGlobalY) }
        for (i in sortedMap.indices) {
            val currentPos = sortedMap[i].first
            val currentFrame = sortedMap[i].second.getInner()

            for (j in currentFrame.indices) {
                for (k in currentFrame[j].indices) {
                    val globalX = currentPos.y * dimFrameX + j
                    val globalY = currentPos.x * dimFrameY + k
                    image[globalX][globalY] = currentFrame[j][k]
                }
            }
        }

        val seaMonster =
            ("""(?=(#.{""" + (dimGlobalX - 19) + """}#....##....##....###.{""" + (dimGlobalX - 19) + """}#..#..#..#..#..#))""").toRegex()

        var seaMonsterCount = 0
        for (i in 0 until 8) {
            val reorientatedImage = reorientate(image, i)

            val sb = StringBuilder()
            for (line in reorientatedImage) {
                sb.append(line)
            }

            val count = seaMonster.findAll(sb.toString()).count()
            if (count != 0) {
                seaMonsterCount = count
                break
            }
        }

        if (seaMonsterCount == 0) {
            throw IllegalArgumentException("No sea monsters found")
        }

        return image.sumBy { line -> line.count { char -> char == '#' } } - seaMonsterCount * 15
    }

    private fun getAdjacentTiles(map: HashMap<Vector2D, Tile>): Set<Triple<Vector2D, Vector2D, Int>> {
        val ret = mutableSetOf<Triple<Vector2D, Vector2D, Int>>()
        val offsets = arrayOf(
            Vector2D(0, -1),
            Vector2D(1, 0),
            Vector2D(0, 1),
            Vector2D(-1, 0)
        )

        for (position in map.keys) {
            for (i in offsets.indices) {
                if (!map.containsKey(position.plus(offsets[i]))) {
                    ret.add(Triple(position, position.plus(offsets[i]), i))
                }
            }
        }

        return ret
    }

    private fun arrange(tiles: MutableSet<Tile>): HashMap<Vector2D, Tile> {
        val map = HashMap<Vector2D, Tile>()
        val first = tiles.first()
        map[Vector2D(0, 0)] = first
        tiles.remove(first)

        while (tiles.isNotEmpty()) {
            val adjacentTiles = getAdjacentTiles(map)

            for (tile in tiles) {
                for (adjacent in adjacentTiles) {
                    val orientation = map[adjacent.first]!!.fitsTo(tile, adjacent.third)
                    if (orientation != null) {
                        tile.orientation = orientation
                        map[adjacent.second] = tile
                    }
                }
            }

            tiles.removeAll(map.values)
        }
        return map
    }

    data class Tile(val id: Int, private val borders: Array<Array<BooleanArray>>, private val inner: Array<CharArray>) {
        var orientation = 0

        private fun getCurrentBorders() = borders[orientation]

        fun fitsTo(other: Tile, position: Int, orientation: Int? = null): Int? {
            if (orientation != null) {
                return if (getCurrentBorders()[position].contentEquals(other.borders[orientation][(position + 2) % 4])) {
                    orientation
                } else {
                    null
                }
            } else {
                for (currentOrientation in 0 until 8) {
                    if (getCurrentBorders()[position].contentEquals(other.borders[currentOrientation][(position + 2) % 4])) {
                        return currentOrientation
                    }
                }

                return null
            }
        }

        fun getInner(): Array<CharArray> {
            return reorientate(inner, orientation)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Tile

            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            return id
        }
    }
}