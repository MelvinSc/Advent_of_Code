package io.github.melvinsc.year2019.day08

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day08)

object Day08 : Day() {
    override fun first(inputData: String): Int {
        val program = inputData.toCharArray().map { it.toInt() - 48 }.toIntArray()
        val width = 25
        val height = 6
        val pixelPerLayer = width*height
        val layerCount = program.size/width/height

        var minZeros: Int? = null
        var hash: Int? = null

        for (i in 0 until layerCount) {
            val currentLayer = program.slice(i*pixelPerLayer until i*pixelPerLayer+pixelPerLayer)
            val currentZeros = currentLayer.count { j -> j == 0 }

            if (minZeros == null || currentZeros < minZeros) {
                minZeros = currentZeros
                hash = currentLayer.count { j -> j == 1 } * currentLayer.count { j -> j == 2 }
            }
        }

        return hash ?: throw IllegalStateException("No max was found")
    }

    override fun second(inputData: String): String {
        val program = inputData.toCharArray().map { it.toInt() - 48 }.toIntArray()
        val width = 25
        val height = 6
        val pixelPerLayer = width*height
        val layerCount = program.size/width/height
        val result = Array(pixelPerLayer) { 2 }

        for (i in 0 until layerCount) {
            val currentLayer = program.slice(i*pixelPerLayer until i*pixelPerLayer+pixelPerLayer)

            for (j in 0 until pixelPerLayer) {
                if (result[j] == 2) {
                    result[j] = currentLayer[j]
                }
            }
        }

        val ret = StringBuilder()
        for (i in 0 until pixelPerLayer) {
            if (i%width == 0) ret.append("\n\t\t")
            if (result[i] != 2) {
                ret.append(result[i])
            } else {
                ret.append(" ")
            }
        }

        return ret.replace(Regex("0"), " ").replace(Regex("1"), "\u2588")
    }
}