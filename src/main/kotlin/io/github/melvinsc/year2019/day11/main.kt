package io.github.melvinsc.year2019.day11

import io.github.melvinsc.utils.Vector2D
import io.github.melvinsc.utils.day.Day
import io.github.melvinsc.year2019.IntCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = Day.setMain(Day10)

object Day10 : Day() {

    private const val COLOR_BLACK = 0L
    private const val COLOR_WHITE = 1L
    private const val COLOR_DEFAULT = COLOR_BLACK
    private const val TURN_LEFT = 0L
    private const val TURN_RIGHT = 1L

    private val DIRECTION_UP = Vector2D(0, 1)
    private val DIRECTION_RIGHT = Vector2D(1, 0)
    private val DIRECTION_DOWN = Vector2D(0, -1)
    private val DIRECTION_LEFT = Vector2D(-1, 0)

    override fun first(inputData: String): Int {
        val program = inputData.split(",").map { it.toLong() }.toLongArray()
        val input = Channel<Long>(Channel.UNLIMITED)
        val output = Channel<Long>(Channel.UNLIMITED)
        var currentPosition = Vector2D(0, 0)
        var currentDirection = DIRECTION_UP
        var currentColor = COLOR_BLACK
        val positions = HashSet<Vector2D>().apply { add(currentPosition) }
        val colors = HashMap<Vector2D, Int>().apply { put(currentPosition, currentColor.toInt()) }

        runBlocking {
            input.send(currentColor)

            val intCode = launch {
                IntCode().evalSuspendable(program, input, output)
            }.apply { start() }

            launch {
                while (!intCode.isCompleted) {
                    currentColor = output.receive()
                    colors[currentPosition] = currentColor.toInt()

                    currentDirection = turn(currentDirection, output.receive())
                    currentPosition += currentDirection

                    positions.add(currentPosition)
                    input.send(colors[currentPosition]?.toLong() ?: COLOR_DEFAULT)
                }
            }
        }

        return positions.size
    }

    override fun second(inputData: String): String {
        val program = inputData.split(",").map { it.toLong() }.toLongArray()
        val input = Channel<Long>(Channel.UNLIMITED)
        val output = Channel<Long>(Channel.UNLIMITED)
        var currentPosition = Vector2D(0, 0)
        var currentDirection = DIRECTION_UP
        var currentColor = COLOR_WHITE
        val colors = HashMap<Vector2D, Int>()

        runBlocking {
            input.send(currentColor)

            val intCode = launch {
                IntCode().evalSuspendable(program, input, output)
            }.apply { start() }

            launch {
                while (!intCode.isCompleted) {
                    currentColor = output.receive()
                    colors[currentPosition] = currentColor.toInt()

                    currentDirection = turn(currentDirection, output.receive())
                    currentPosition += currentDirection

                    input.send(colors[currentPosition]?.toLong() ?: COLOR_DEFAULT)
                }
            }
        }
        
        val maxX = colors.map{ it.key.x }.max() ?: throw IllegalStateException("No elements were found")
        val minX = colors.map{ it.key.x }.min() ?: throw IllegalStateException("No elements were found")
        val maxY = colors.map{ it.key.y }.max() ?: throw IllegalStateException("No elements were found")
        val minY = colors.map{ it.key.y }.min() ?: throw IllegalStateException("No elements were found")

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                colors.putIfAbsent(Vector2D(x, y), COLOR_BLACK.toInt())
            }
        }

        val stringBuilder = StringBuilder()
        var lastY : Int? = null
        colors.toSortedMap(compareBy<Vector2D>{ -it.y }.thenBy { it.x }).forEach { (position, color) ->
            if (lastY == null || lastY != position.y) {
                stringBuilder.append("\n\t\t")
                lastY = position.y
            }
            if (color == COLOR_BLACK.toInt()) {
                stringBuilder.append(" ")
            } else {
                stringBuilder.append("\u2588")
            }
        }


        return stringBuilder.toString()
    }

    private fun turn(currentDirection: Vector2D, turn: Long) = when (currentDirection) {
        DIRECTION_UP -> if (turn == TURN_LEFT) DIRECTION_LEFT else DIRECTION_RIGHT
        DIRECTION_RIGHT -> if (turn == TURN_LEFT) DIRECTION_UP else DIRECTION_DOWN
        DIRECTION_DOWN -> if (turn == TURN_LEFT) DIRECTION_RIGHT else DIRECTION_LEFT
        DIRECTION_LEFT ->if (turn == TURN_LEFT) DIRECTION_DOWN else DIRECTION_UP
        else -> throw IllegalStateException("$currentDirection is not a valid direction")
    }
}