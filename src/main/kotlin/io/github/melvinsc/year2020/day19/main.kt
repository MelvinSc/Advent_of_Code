package io.github.melvinsc.year2020.day19

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day19)

typealias Input = Pair<RuleBook, MessageList>
typealias MessageList = Set<String>
typealias RuleBook = Map<Int, Day19.Rule>

object Day19 : Day() {

    data class Rule(val id: Int, val dependencies: List<List<Int>>, val value: String?)

    private fun convert(inputData: String, replacers: List<Rule>? = null): Input {
        val parts = inputData.split("\n\n").map { it.lines() }

        return Pair(parts.first().map { line ->
            val components = line.split(':')
            val id = components.first().toInt()

            val replacer = replacers?.find { rule -> rule.id == id }

            id to when {
                replacer != null -> {
                    replacer
                }
                components.last().contains("""[ab]""".toRegex()) -> {
                    Rule(id, listOf(), components.last()[2].toString())
                }
                else -> {
                    val ruleParts = components.last().trim().split('|').map { it.trim() }
                    Rule(id, ruleParts.map { rulePart -> rulePart.split(' ').map { it.toInt() } }, null)
                }
            }


        }.toMap(), parts.last().toSet())
    }

    override fun first(inputData: String) = solve(convert(inputData))

    override fun second(inputData: String) = solve(
        convert(
            inputData,
            listOf(
                Rule(8, listOf(listOf(42, 8), listOf(42)), null),
                Rule(11, listOf(listOf(42, 11, 31), listOf(42, 31)), null)
            )
        )
    )

    private fun solve(input: Input) = input.second.count { message ->
        match(message, 0, 0, input.first).contains(message.length)
    }

    private fun match(string: String, position: Int, id: Int, ruleMap: Map<Int, Rule>): Set<Int> {
        val rule = ruleMap[id] ?: throw IllegalArgumentException("Rule $id does not exist")

        return rule.value?.let { value ->
            if (string.regionMatches(position, value, 0, value.length)) {
                setOf(position + value.length)
            } else {
                emptySet()
            }
        } ?: run {
            rule.dependencies.fold(mutableSetOf()) { result, rules ->
                result.apply {
                    var positions = setOf(position)
                    for (nextRuleId in rules) {
                        val newPositions =
                            positions.map { position -> match(string, position, nextRuleId, ruleMap) }.flatten().toSet()

                        if (newPositions.isNotEmpty()) {
                            positions = newPositions
                        } else {
                            positions = emptySet()
                            break
                        }
                    }
                    addAll(positions)
                }
            }
        }
    }
}