package io.github.melvinsc.year2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class IntCode {
    suspend fun evalSuspendable(
        program: IntArray,
        input: ReceiveChannel<Int> = Channel(Channel.UNLIMITED),
        output: SendChannel<Int> = Channel(Channel.UNLIMITED)
    ) {
        data class Op(
            val length: Int,
            val eval: suspend (program: IntArray, position: Int, modes: List<Int>) -> Int?
        )

        fun getValue(program: IntArray, value: Int, mode: Int) = when (mode) {
            0 -> program[program[value]]
            1 -> program[value]
            else -> throw UnsupportedOperationException("Mode $mode is not supported")
        }

        val ops = mapOf(
            1 to Op(4) { currentProgram, position, modes ->
                currentProgram[currentProgram[position + 3]] =
                    getValue(currentProgram, position + 1, modes[0]) + getValue(currentProgram, position + 2, modes[1])
                null
            },
            2 to Op(4) { currentProgram, position, modes ->
                currentProgram[currentProgram[position + 3]] =
                    getValue(currentProgram, position + 1, modes[0]) * getValue(currentProgram, position + 2, modes[1])
                null
            },
            3 to Op(2) { currentProgram, position, _ ->
                currentProgram[currentProgram[position + 1]] = input.receive()
                null
            },
            4 to Op(2) { currentProgram, position, modes ->
                output.send(getValue(currentProgram, position + 1, modes[0]))
                null
            },
            5 to Op(3) { currentProgram, position, modes ->
                getValue(currentProgram, position + 1, modes[0]).takeIf { it != 0 }
                    ?.let { getValue(currentProgram, position + 2, modes[1]) }
            },
            6 to Op(3) { currentProgram, position, modes ->
                getValue(currentProgram, position + 1, modes[0]).takeIf { it == 0 }
                    ?.let { getValue(currentProgram, position + 2, modes[1]) }
            },
            7 to Op(4) { currentProgram, position, modes ->
                currentProgram[currentProgram[position + 3]] =
                    if (getValue(currentProgram, position + 1, modes[0]) < getValue(
                            currentProgram,
                            position + 2,
                            modes[1]
                        )
                    ) 1 else 0
                null
            },
            8 to Op(4) { currentProgram, position, modes ->
                currentProgram[currentProgram[position + 3]] =
                    if (getValue(currentProgram, position + 1, modes[0]) == getValue(
                            currentProgram,
                            position + 2,
                            modes[1]
                        )
                    ) 1 else 0
                null
            }
        )

        fun parseOp(rawOp: Int): Pair<Op, List<Int>>? {
            val opcode = rawOp % 100
            val op = ops[opcode] ?: return null
            val rawModes = rawOp / 100
            val modes = mutableListOf<Int>()
            (1 until op.length).fold(rawModes) { curr, _ ->
                modes += curr % 10
                return@fold curr / 10
            }
            return op to modes
        }

        var index = 0
        while (index in program.indices) {
            val rawOp = program[index]
            parseOp(rawOp)?.let { (op, modes) ->
                val jump = op.eval(program, index, modes)
                index = jump ?: index + op.length
            } ?: when (rawOp) {
                99 -> return
                else -> throw UnsupportedOperationException("Opcode $rawOp is not supported")
            }
        }

        throw IndexOutOfBoundsException("Trying to reach position out of memory bounds")
    }

    fun eval (program: IntArray,
              input: ReceiveChannel<Int> = Channel(Channel.UNLIMITED),
              output: SendChannel<Int> = Channel(Channel.UNLIMITED)
    ) {
        runBlocking {
            launch {
                evalSuspendable(program, input, output)
            }
        }
    }
}