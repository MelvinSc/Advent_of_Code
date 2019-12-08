package io.github.melvinsc.year2019

object IntCode {
    fun eval(program: IntArray) {
        for (i in 0..program.size step 4) {
            when (program[i]) {
                1 -> program[program[i + 3]] = program[program[i + 1]] + program[program[i + 2]]
                2 -> program[program[i + 3]] = program[program[i + 1]] * program[program[i + 2]]
                99 -> return
                else -> throw IllegalArgumentException("Wrong opcode at: $i = ${program[i]}")
            }
        }
    }
}