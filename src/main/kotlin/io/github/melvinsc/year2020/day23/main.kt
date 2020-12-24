package io.github.melvinsc.year2020.day23

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day23)

object Day23 : Day() {

    private fun convert(inputData: String) = inputData.map { Character.getNumericValue(it) - 1 }

    override fun first(inputData: String): String {
        val labels = convert(inputData)
        val indices = getIndices(labels, labels.size)
        play(indices, labels.first(), 100)
        return getFirstResult(indices, indices.first())
    }

    override fun second(inputData: String): Long {
        val labels = convert(inputData)
        val indices = getIndices(labels, 1_000_000)
        play(indices, labels.first(), 10_000_000)
        return (indices.first().toLong() + 1) * (indices[indices.first()].toLong() + 1)
    }

    private fun getIndices(initialCups: List<Int>, size: Int): IntArray {
        val allCups = IntArray(size) { index -> if (index in initialCups.indices) initialCups[index] else index }
        val result = IntArray(size)
        allCups.forEachIndexed { index, label -> result[label] = allCups[(index + 1) % allCups.size] }
        return result
    }

    private fun getFirstResult(index: IntArray, head: Int, acc: String = ""): String =
        if (head == 0) {
            acc
        } else {
            getFirstResult(index, index[head], acc + (head + 1))
        }

    private fun play(indices: IntArray, firstLabel: Int, count: Int) {
        var currentLabel = firstLabel
        repeat(count) {
            val pick1 = indices[currentLabel]
            val pick2 = indices[pick1]
            val pick3 = indices[pick2]

            var destinationLabel = Math.floorMod(currentLabel - 1, indices.size)
            while (pick1 == destinationLabel || pick2 == destinationLabel || pick3 == destinationLabel) {
                destinationLabel = Math.floorMod(destinationLabel - 1, indices.size)
            }

            val old = indices[destinationLabel]
            indices[destinationLabel] = pick1
            indices[currentLabel] = indices[pick3]
            indices[pick3] = old

            currentLabel = indices[currentLabel]
        }
    }
}