package io.github.melvinsc.year2020.day22

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day22)

typealias Deck = MutableList<Int>

object Day22 : Day() {
    private fun convert(inputData: String) =
        inputData.split("\n\n").map { parts -> parts.lines().drop(1).map { it.toInt() }.toMutableList() }

    override fun first(inputData: String): Int {
        val (first, second) = convert(inputData)

        while (first.isNotEmpty() && second.isNotEmpty()) {
            doSimple(first, second)
        }

        return if (second.isEmpty()) {
            getWinningScore(first)
        } else {
            getWinningScore(second)
        }
    }

    override fun second(inputData: String): Int {
        val (first, second) = convert(inputData)

        val winner = doAdvancedGame(first, second)

        return if (winner) {
            getWinningScore(first)
        } else {
            getWinningScore(second)
        }
    }

    private fun doSimple(first: Deck, second: Deck) {
        val f = first.removeFirst()
        val s = second.removeFirst()

        when {
            f > s -> {
                first.add(f)
                first.add(s)
            }
            f < s -> {
                second.add(s)
                second.add(f)
            }
        }
    }

    private fun doAdvancedGame(first: Deck, second: Deck): Boolean {
        val history = HashSet<Pair<Deck, Deck>>()
        history.add(Pair(first, second))

        while (first.isNotEmpty() && second.isNotEmpty()) {
            val f = first.removeFirst()
            val s = second.removeFirst()

            val winner = if (first.size >= f && second.size >= s) {
                doAdvancedGame(first.take(f).toMutableList(), second.take(s).toMutableList())
            } else {
                f > s
            }

            if (winner) {
                first.add(f)
                first.add(s)
            } else {
                second.add(s)
                second.add(f)
            }

            if (history.contains(Pair(first, second))) {
                return true
            } else {
                history.add(Pair(first, second))
            }
        }

        return second.isEmpty()
    }

    private fun getWinningScore(deck: Deck) = deck.reversed().withIndex().sumBy { (index, card) -> (index + 1) * card }
}