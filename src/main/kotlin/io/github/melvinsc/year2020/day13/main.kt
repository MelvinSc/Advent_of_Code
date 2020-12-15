package io.github.melvinsc.year2020.day13

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day13)

object Day13 : Day() {
    override fun first(inputData: String): Int {
        val lines = inputData.lines()
        val departureTime = lines.first().toInt()
        val ids = lines.last().split(',').mapNotNull { id ->
            if (id == "x") {
                null
            } else {
                id.toInt()
            }
        }

        var bestID = ids.first()
        var leastTime = Math.floorMod(-departureTime, bestID)
        for (i in 1 until ids.size) {
            val currentTime = Math.floorMod(-departureTime, ids[i])
            if (currentTime < leastTime) {
                bestID = ids[i]
                leastTime = currentTime
            }
        }

        return bestID * Math.floorMod(-departureTime, bestID)
    }

    override fun second(inputData: String): Long {
        val busses = inputData.split("\n").last().split(',').mapIndexedNotNull { index, id ->
            if (id == "x") {
                null
            } else {
                Bus(id.toLong(), index.toLong())
            }
        }

        var lastCandidate: Long = busses.first().id
        var lastPeriod: Long = busses.first().id
        for (busIndex in 1 until busses.size) {
            val currentBus = busses[busIndex]
            for (departureTime in lastCandidate..Long.MAX_VALUE step lastPeriod) {
                if ((departureTime + currentBus.offset) % currentBus.id == 0L) {
                    lastPeriod *= currentBus.id
                    lastCandidate = departureTime
                    break
                }
            }
        }

        return lastCandidate
    }

    class Bus(val id: Long, val offset: Long)
}