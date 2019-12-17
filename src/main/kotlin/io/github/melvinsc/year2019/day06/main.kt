package io.github.melvinsc.year2019.day06

import io.github.melvinsc.utils.Graph
import io.github.melvinsc.utils.BufferTree
import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day06)

object Day06 : Day() {
    override fun first(inputData: String): Int {
        val tree = inputData.split("\n")
            .map { line -> line.split(")") }
            .fold(BufferTree<String>()) { acc, orbit ->
                acc.insert(orbit[0], orbit[1], orbit[0] == "COM")
                return@fold acc
            }

        var sum = 0
        val list = mutableListOf<Pair<BufferTree.Node<String>?, Int>>()
        list.add(Pair(tree.getNode("COM"), 0))
        if (tree.isValid()) {
            while (list.isNotEmpty()) {
                val current = list.first().also { list.removeAt(0) }
                sum += current.second

                for (element in current.first?.getChilds() ?: throw IllegalStateException("Something is fucked up")) {
                    list.add(Pair(element, current.second + 1))
                }
            }

            return sum
        } else {
            throw IllegalStateException("The Tree is in an Illegal State")
        }
    }

    override fun second(inputData: String): Int {
        val graph = inputData.split("\n")
            .map { line -> line.split(")") }
            .fold(Graph<String>()) { acc, orbit ->
                acc.addEdge(orbit[0], orbit[1])
                return@fold acc
            }

        val youPlanet = graph["YOU"]?.asSequence()?.first()
        val sanPlanet = graph["SAN"]?.asSequence()?.first()

        if (youPlanet != null && sanPlanet != null) {
            val map = HashMap<String, Int>()
            val setToAdd = HashSet<String>().apply { add(youPlanet) }
            val lastSet = HashSet<String>()
            var currentDepth = 0

            while (!map.contains(sanPlanet)) {
                setToAdd.forEach { map.putIfAbsent(it, currentDepth) }
                lastSet.addAll(setToAdd)
                setToAdd.clear()

                lastSet.forEach {
                    val tmp = graph[it] ?: throw IllegalStateException("Something is fucked up")
                    setToAdd.addAll(tmp.map { s -> s })
                }

                ++currentDepth
            }

            return currentDepth -1
        } else {
            throw IllegalStateException("You no have Santa or yourself:(")
        }
    }
}