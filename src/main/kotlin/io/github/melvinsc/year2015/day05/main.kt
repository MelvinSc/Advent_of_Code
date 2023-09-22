package io.github.melvinsc.year2015.day05

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day05)

object Day05 : Day() {

    override fun first(inputData: String): Int {
        val vowels = "aeiou"
        val pair = Regex("""(.)\1""")
        val badStrings = Regex("""ab|cd|pq|xy""")

        return inputData.split('\n')
            .filter { line -> line.count { it in vowels } >= 3 }
            .filter { pair.containsMatchIn(it) }
            .filter { !badStrings.containsMatchIn(it) }
            .count()
    }

    override fun second(inputData: String): Int {
        val regex = Regex("""(..).*\1""")
        val regex2 = Regex("""(.).\1""")

        return inputData.split('\n')
            .filter { regex.containsMatchIn(it) }
            .filter { regex2.containsMatchIn(it) }
            .count()
    }
}