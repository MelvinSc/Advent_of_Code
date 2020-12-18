package io.github.melvinsc.year2020.day18

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day18)


object Day18 : Day() {

    override fun first(inputData: String) = inputData.replace(" ", "").lines()
        .map { line -> SimpleExpression(line) }.sumOf { expression -> expression.evaluate() }

    override fun second(inputData: String) = inputData.replace(" ", "").lines()
        .map { line -> AdvancedEvaluator(line) }.sumOf { expression -> expression.evaluate() }
}