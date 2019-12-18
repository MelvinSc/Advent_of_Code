package io.github.melvinsc.year2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class IntCode {
    private var index = 0L
    private var relBase = 0L
    private val DEFAULT_VALUE = 0L
    private val memory = HashMap<Long, Long>()

    suspend fun evalSuspendable(
        program: LongArray,
        input: ReceiveChannel<Long> = Channel(Channel.UNLIMITED),
        output: SendChannel<Long> = Channel(Channel.UNLIMITED)
    ) {
        data class Op(
            val length: Int,
            val eval: suspend (modes: List<Int>) -> Long?
        )

        fun getValue(currentIndex: Long, mode: Int) = when (mode) {
            0 -> memory.getOrDefault(memory.getOrDefault(currentIndex, DEFAULT_VALUE), DEFAULT_VALUE)
            1 -> memory.getOrDefault(currentIndex, DEFAULT_VALUE)
            2 -> memory.getOrDefault(relBase + memory.getOrDefault(currentIndex, DEFAULT_VALUE), DEFAULT_VALUE)
            else -> throw UnsupportedOperationException("Mode $mode is not supported")
        }

        fun getDestination(currentIndex: Long, mode: Int) = when (mode) {
            0 -> memory.getOrDefault(currentIndex, DEFAULT_VALUE)
            2 -> relBase + memory.getOrDefault(currentIndex, DEFAULT_VALUE)
            else -> throw UnsupportedOperationException("Mode $mode is not supported")
        }

        val ops = mapOf(
            1 to Op(4) { modes ->
                memory[getDestination(index + 3, modes[2])] =
                    getValue(index + 1, modes[0]) + getValue(index + 2, modes[1])
                null
            },
            2 to Op(4) { modes ->
                memory[getDestination(index + 3, modes[2])] =
                    getValue(index + 1, modes[0]) * getValue(index + 2, modes[1])
                null
            },
            3 to Op(2) { modes ->
                memory[getDestination(index + 1, modes[0])] = input.receive()
                null
            },
            4 to Op(2) { modes ->
                output.send(getValue(index + 1, modes[0]))
                null
            },
            5 to Op(3) { modes ->
                getValue(index + 1, modes[0]).takeIf { it != 0L }
                    ?.let { getValue(index + 2, modes[1]) }
            },
            6 to Op(3) { modes ->
                getValue(index + 1, modes[0]).takeIf { it == 0L }
                    ?.let { getValue(index + 2, modes[1]) }
            },
            7 to Op(4) { modes ->
                memory[getDestination(index + 3, modes[2])] =
                    if (getValue(index + 1, modes[0]) <
                        getValue(index + 2, modes[1])
                    )
                        1L else 0L
                null
            },
            8 to Op(4) { modes ->
                memory[getDestination(index + 3, modes[2])] =
                    if (getValue(index + 1, modes[0]) ==
                        getValue(index + 2, modes[1])
                    )
                        1L else 0L
                null
            },
            9 to Op(2) { modes ->
                relBase += getValue(index + 1, modes[0])
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

        program.forEachIndexed { index, i -> memory[index.toLong()] = i }

        while (true) {
            val rawOp = memory.getOrDefault(index, DEFAULT_VALUE)
            parseOp(rawOp.toInt())?.let { (op, modes) ->
                val jump = op.eval(modes)
                index = jump ?: index + op.length
            } ?: when (rawOp) {
                99L -> {
                    program.indices.forEachIndexed { i, _ -> program[i] = memory[i.toLong()] ?: return@forEachIndexed }
                    return
                }
                else -> throw UnsupportedOperationException("Opcode $rawOp is not supported")
            }
        }
    }

    fun eval(
        program: LongArray,
        input: ReceiveChannel<Long> = Channel(Channel.UNLIMITED),
        output: SendChannel<Long> = Channel(Channel.UNLIMITED)
    ) {
        runBlocking {
            launch {
                evalSuspendable(program, input, output)
            }
        }
    }
}