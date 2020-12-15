package io.github.melvinsc.year2020.day06

import io.github.melvinsc.utils.day.Day
import java.util.regex.Pattern

fun main() = Day.setMain(Day06)

object Day06 : Day() {
    private fun convert(inputData: String) = inputData.split(Pattern.compile("""\n\n""")).map { group ->
        group.lines()
    }

    override fun first(inputData: String) = convert(inputData).sumBy { group -> countAny(group) }

    override fun second(inputData: String) = convert(inputData).sumBy { group -> countAll(group) }

    private fun countAny(group: List<String>) = group.flatMap { people -> people.toList() }.distinct().count()

    private fun countAll(group: List<String>) = if (group.size == 1) {
        group.first().length
    } else {
        var current = group.first().toSet()

        for (i in 1 until group.size) {
            current = current.intersect(group[i].toSet())
        }

        current.size
    }
}