package io.github.melvinsc.year2020.day04

import io.github.melvinsc.utils.day.Day
import java.util.regex.Pattern

fun main() = Day.setMain(Day04)

object Day04 : Day() {
    private fun convert(inputData: String) = inputData.split("\n\n").map { data ->
        data.split(Pattern.compile("""[\n ]""")).map { s -> Pair(s.take(3), s.substring(4)) }.toMap()
    }.toHashSet()

    override fun first(inputData: String) = convert(inputData).count { passport ->
        when (passport.size) {
            8 -> true
            7 -> !passport.containsKey("cid")
            else -> false
        }
    }

    override fun second(inputData: String) = convert(inputData).count { passport ->
        if (passport.size < 7) {
            return@count false
        } else if (passport.size == 7 && passport.containsKey("cid")) {
            return@count false
        } else {
            val byr = passport["byr"]!!.toInt()
            if (byr < 1920 || byr > 2002) {
                return@count false
            }

            val iyr = passport["iyr"]!!.toInt()
            if (iyr < 2010 || iyr > 2020) {
                return@count false
            }

            val eyr = passport["eyr"]!!.toInt()
            if (eyr < 2020 || eyr > 2030) {
                return@count false
            }

            try {
                val hgt = passport["hgt"]!!.dropLast(2).toInt()
                val hgtUnit = passport["hgt"]!!.endsWith("cm")
                if (hgtUnit && (hgt < 150 || hgt > 193) || !hgtUnit && (hgt < 59 || hgt > 76)) {
                    return@count false
                }
            } catch (e: NumberFormatException) {
                return@count false
            }

            val hcl = passport["hcl"]!!
            if (!Regex("""^#(\d|[a-f]){6}$""").matches(hcl)) {
                return@count false
            }

            val ecl = passport["ecl"]!!
            if (!Regex("""^(amb|blu|brn|gry|grn|hzl|oth)$""").matches(ecl)) {
                return@count false
            }

            val pid = passport["pid"]!!
            if (!Regex("""^\d{9}$""").matches(pid)) {
                return@count false
            }
        }

        return@count true
    }
}
