package io.github.melvinsc.year2015.day04

import io.github.melvinsc.utils.countLeadingZeroBits
import io.github.melvinsc.utils.day.Day
import java.security.MessageDigest

fun main() = Day.setMain(Day04)

object Day04 : Day() {

    private val md5 = MessageDigest.getInstance("MD5")

    override fun first(inputData: String): Int = getNonce(5)

    override fun second(inputData: String): Int = getNonce(6)

    private fun getNonce(leadingZeroes: Int) =
        generateSequence(1) { it + 1 }
            .first { md5.digest("$input$it".toByteArray()).countLeadingZeroBits() >= 4 * leadingZeroes }
}
