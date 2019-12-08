package io.github.melvinsc.utils.day

import kotlin.system.measureTimeMillis

/**
 * Abstract class representing solution for [day]s problem in specified [year].
 */
abstract class Day {
    val year = javaClass.name.split(".")[3].takeLast(4).toInt()
    val day = javaClass.name.split(".")[4].takeLast(2).toInt()
    val input: String = Fetcher.getInput(year, day)

    open fun first(input: String): Any? {
        return null
    }

    open fun second(input: String): Any? {
        return null
    }

    companion object {
        fun setMain(someday: Day) {
            with(someday) {
                println("Year $year, Day $day")
                measureTimeMillis {
                    println("First:")
                    println("\tSolution: ${first(input)?.toString() ?: "unsolved"}")
                }.run {
                    println("\tTime:     ${this}ms")
                }
                measureTimeMillis {
                    println("Second:")
                    println("\tSolution: ${second(input)?.toString() ?: "unsolved"}")
                }.run {
                    println("\tTime:     ${this}ms")
                }
            }
        }
    }
}