package io.github.melvinsc.year2020.day08

object IntCode2 {
    fun execute(instructions: List<String>): Pair<Int, Int> {
        var pointer = 0
        var accumulator = 0
        val visitedPointers = HashSet<Int>()

        while (!visitedPointers.contains(pointer)) {
            visitedPointers.add(pointer)
            if (pointer in instructions.indices) {
                val instruction = instructions[pointer].split(' ')

                when (instruction[0]) {
                    "acc" -> {
                        accumulator += instruction[1].toInt()
                        pointer++
                    }
                    "jmp" -> pointer += instruction[1].toInt()
                    "nop" -> pointer++
                }
            } else {
                return Pair(0, accumulator)
            }
        }

        return Pair(1, accumulator)
    }
}