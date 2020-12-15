package io.github.melvinsc.year2020.day14

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day14)

object Day14 : Day() {
    @ExperimentalStdlibApi
    @ExperimentalUnsignedTypes
    override fun first(inputData: String): ULong {
        val instructions = getInstructions(inputData)

        val memory = HashMap<Int, ULong>()
        val currentMask = Array<ULong>(2) { 0u }

        for (instruction in instructions) {
            if (instruction.first == -1) {
                currentMask[0] = instruction.second.replace('X', '1').convertToULong()
                currentMask[1] = instruction.second.replace('X', '0').convertToULong()
            } else {
                memory[instruction.first] = instruction.second.toULong().and(currentMask[0]).or(currentMask[1])
            }
        }

        return memory.values.sum()
    }

    @ExperimentalStdlibApi
    @ExperimentalUnsignedTypes
    override fun second(inputData: String): ULong {
        val groups = inputData.split("mask = ").drop(1).map { set ->
            val elements = set.trim().lines()

            Pair(elements.first(), elements.drop(1).map { instruction ->
                val lr = instruction.split(" = ")
                Pair(lr.first().substring(4, lr.first().length - 1).toULong(), lr.last().toULong())
            })
        }

        val memory = HashMap<ULong, ULong>()
        for (group in groups) {
            for (instruction in group.second) {
                for (address in combinedSequence(group.first, instruction.first)) {
                    memory[address] = instruction.second
                }
            }
        }

        return memory.values.sum()
    }

    @ExperimentalStdlibApi
    @ExperimentalUnsignedTypes
    private fun combinedSequence(mask: String, address: ULong) = sequence {
        val binAddress = toBinary(address).padStart(36, '0')

        val sb = StringBuilder()
        for (i in mask.indices) {
            when (mask[i]) {
                'X' -> sb.append('X')
                '1' -> sb.append('1')
                '0' -> sb.append(binAddress[i])
            }
        }
        val combinedMask = sb.toString()

        fun getPermutations(combinedMask: String): Sequence<ULong> = sequence {
            if (combinedMask.contains('X')) {
                yieldAll(getPermutations(combinedMask.replaceFirst('X', '0')))
                yieldAll(getPermutations(combinedMask.replaceFirst('X', '1')))
            } else {
                yield(combinedMask.convertToULong())
            }
        }

        yieldAll(getPermutations(combinedMask))
    }


    @ExperimentalUnsignedTypes
    fun toBinary(decimalNumber: ULong, binaryString: String = ""): String {
        while (decimalNumber > 0u) {
            val temp = "${binaryString}${decimalNumber % 2u}"
            return toBinary(decimalNumber / 2u, temp)
        }
        return binaryString.reversed()
    }

    private fun getInstructions(inputData: String) = inputData.lines().map { line ->
        val instruction = line.split(" = ")

        if (instruction.first() == "mask") {
            Pair(-1, instruction.last())
        } else {
            val address = instruction.first().substring(4, instruction.first().length - 1).toInt()
            Pair(address, instruction.last())
        }
    }

    @ExperimentalStdlibApi
    @ExperimentalUnsignedTypes
    private fun String.convertToULong(): ULong {
        var ret: ULong = 0u

        for (i in this.indices) {
            if (this[i] == '1') {
                ret = ret.or(1u)
            }
            ret = ret.rotateLeft(1)
        }

        return ret.rotateRight(1)
    }
}
