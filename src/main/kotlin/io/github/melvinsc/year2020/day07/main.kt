package io.github.melvinsc.year2020.day07

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day07)

typealias Content = HashSet<Pair<Int, String>>

object Day07 : Day() {
    private fun convertInput(rawInput: String): HashMap<String, Content> {
        val rules = HashMap<String, Content>(rawInput.count { c -> c == '\n' || c == ',' })
        rawInput.lines().forEach { rule ->
            if (!rule.endsWith("no other bags.")) {
                val parts = rule.split("contain")
                val outer = parts[0].dropLast(6).filterNot { c -> c == ' ' }
                val inner = Content(parts[1].split(',').map { bag ->
                    val content = bag.drop(1).split(' ')
                    Pair(content[0].toInt(), content[1] + content[2])
                }.toSet())
                rules[outer] = inner
            }
        }
        return rules
    }

    override fun first(inputData: String): Int {
        val canContain = HashSet<String>()
        val input = convertInput(inputData)

        input.forEach { (outer, inner) ->
            if (inner.any { pair -> pair.second == "shinygold" }) {
                canContain.add(outer)
            }
        }

        do {
            val preSize = canContain.size
            input.forEach { (outer, inner) ->
                if (!canContain.contains(outer) && inner.any { pair -> canContain.contains(pair.second) }) {
                    canContain.add(outer)
                }
            }
        } while (canContain.size != preSize)

        return canContain.size
    }

    override fun second(inputData: String) = getCount(convertInput(inputData), "shinygold")

    private fun getCount(input: HashMap<String, Content>, color: String): Int =
        input[color]?.sumBy { pair ->
            pair.first + pair.first * getCount(input, pair.second)
        } ?: 0
}