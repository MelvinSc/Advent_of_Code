package io.github.melvinsc.year2020.day16

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day16)

typealias Rule = (Int) -> Boolean
typealias Ticket = List<Int>
typealias RuleMap = Map<Rule, String>
typealias Document = Triple<RuleMap, Ticket, List<Ticket>>

object Day16 : Day() {

    private fun convert(inputData: String): Document {
        val parts = inputData.split("\n\n")

        val rules = parts.first().lines().map { rule ->
            val elements = rule.split(": ")
            val ranges = elements.last().split(" or ")
                .map { range -> range.split('-') }.flatten().map { it.toInt() }
            Pair({ n: Int -> n in ranges[0]..ranges[1] || n in ranges[2]..ranges[3] }, elements.first())
        }.toMap()

        val myTicket = parts[1].lines().last().split(',').map { it.toInt() }

        val otherTickets = parts.last().lines().drop(1).map { line -> line.split(',').map { it.toInt() } }

        return Document(rules, myTicket, otherTickets)
    }

    override fun first(inputData: String): Int {
        val document = convert(inputData)
        val invalidTickets = separateTickets(document.third, document.first.keys, false)
        return getErrorRate(invalidTickets, document.first.keys)
    }

    override fun second(inputData: String): Long {
        val document = convert(inputData)
        val rules = document.first.keys
        val validTickets = separateTickets(document.third, rules)
        val fieldCount = validTickets.first().size

        val fields = Array<HashSet<Int>>(fieldCount) { hashSetOf() }
        for (fieldIndex in 0 until fieldCount) {
            for (ticket in validTickets) {
                fields[fieldIndex].add(ticket[fieldIndex])
            }
        }

        val ruleMatrix = fields.mapIndexed { index, fieldValues ->
            Pair(
                index,
                rules.filter { rule -> fieldValues.all { value -> rule(value) } }.toMutableSet()
            )
        }.sortedBy { (_, rules) -> rules.size }.toMutableList()

        for (i in 1 until ruleMatrix.size) {
            for (j in i until ruleMatrix.size) {
                ruleMatrix[j].second.remove(ruleMatrix[i - 1].second.first())
            }
        }

        ruleMatrix.removeIf { (_, ruleSet) -> !document.first[ruleSet.first()]!!.startsWith("departure") }

        return ruleMatrix.fold(1) { product, ruleSet -> product * document.second[ruleSet.first] }
    }

    private fun separateTickets(tickets: List<Ticket>, rules: Collection<Rule>, valid: Boolean = true) =
        tickets.filter { ticket -> ticket.all { field -> rules.any { rule -> rule(field) } } == valid }

    private fun getErrorRate(invalidTickets: List<Ticket>, rules: Collection<Rule>) =
        invalidTickets.sumBy { ticket -> ticket.find { field -> !rules.any { rule -> rule(field) } } ?: 0 }

}