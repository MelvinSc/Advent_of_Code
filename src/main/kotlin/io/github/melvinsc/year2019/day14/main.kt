package io.github.melvinsc.year2019.day14

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day14)

object Day14 : Day() {
    override fun first(inputData: String): Int {
        val rawReactions = inputData.lines().map { line ->
            line.split("=>").map { side ->
                side.trim().split(",").map { ingredient ->
                    ingredient.trim().split(" ")
                }
            }
        }

        val reactions = HashSet<Reaction>()
        for (rawReaction in rawReactions) {
            val rawIngredients = rawReaction[0]
            val rawResult = rawReaction[1].first()
            val ingredients = HashSet<Reaction.Chemical>()
            for (ingredient in rawIngredients) {
                ingredients.add(Reaction.Chemical(ingredient[0].toInt(), ingredient[1]))
            }
            val result = Reaction.Chemical(rawResult[0].toInt(), rawResult[1])
            reactions.add(Reaction(ingredients, result))
        }

        return -1
    }

    override fun second(inputData: String): Int {
        return -1
    }

    private data class Reaction(val ingredients: Collection<Chemical>, val result: Chemical) {
        data class Chemical(val ammount: Int, val name: String)
    }
}

