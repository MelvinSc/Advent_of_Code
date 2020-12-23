package io.github.melvinsc.year2020.day21

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day21)

typealias FoodSet = Set<Pair<MutableSet<String>, Set<String>>>

object Day21 : Day() {

    private fun convert(inputData: String) = inputData.lines().map { line ->
        val parts = line.split(" (contains ")
        assert(parts.size == 2)

        Pair(parts.first().split(' ').toMutableSet(), parts.last().dropLast(1).split(", ").toSet())
    }.toSet()

    override fun first(inputData: String): Int {
        val globalFoods = convert(inputData)

        eliminate(globalFoods)

        return globalFoods.sumBy { (ingredients, _) -> ingredients.size }
    }

    private fun getAllergens(foodSet: FoodSet) = foodSet.map { (_, allergens) -> allergens }.flatten().toSet()

    override fun second(inputData: String) = eliminate(convert(inputData)).toSortedMap().values.joinToString(",")


    private fun eliminate(foodSet: FoodSet): Map<String, String> {
        val globalAllergens = getAllergens(foodSet).toMutableSet()

        val map = HashMap<String, String>()
        while (globalAllergens.isNotEmpty()) {
            for (allergen in globalAllergens) {
                val currentFoods = foodSet.filter { (_, allergens) -> allergens.contains(allergen) }

                var currentIngredients = currentFoods.first().first.toSet()
                for (i in 1 until currentFoods.size) {
                    currentIngredients = currentIngredients.intersect(currentFoods[i].first)
                    if (currentIngredients.isEmpty()) {
                        break
                    }
                }

                if (currentIngredients.size == 1) {
                    map[allergen] = currentIngredients.first()

                    for (food in foodSet) {
                        food.first.remove(currentIngredients.first())
                    }
                }
            }
            globalAllergens.removeAll(map.keys)
        }

        return map
    }
}