package io.github.melvinsc.utils

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun <T> Iterable<T>.getGroups(): List<List<T>> {
    val ret = mutableListOf<MutableList<T>>()
    var list = mutableListOf<T>()
    for (element in this) {
        if (list.contains(element)) {
            list.add(element)
        } else {
            list = mutableListOf(element)
            ret.add(list)
        }
    }

    return ret
}

fun <T> linkedListOf(vararg items: T) = LinkedList<T>().apply {
    for (i in items) {
        add(i)
    }
}

class BufferTree<T> {
    private var root: Node<T>? = null
    private val orphans = mutableListOf<Pair<T, T>>()

    fun insert(element: T, child: T, isRoot: Boolean) {
        if (isRoot) {
            setRoot(element, child)
        } else {
            if (putToParent(element, child) && !isValid()) {
                tryReinsert()
            } else {
                orphans.add(Pair(element, child))
            }
        }
    }

    fun isValid(): Boolean = orphans.isEmpty()

    fun setRoot(element: T, child: T) {
        root = Node(element, child)
        tryReinsert()
    }

    private fun putToParent(element: T, child: T): Boolean {
        val parentNode = getNode(element)

        if (parentNode != null) {
            parentNode.addChild(Node(child))
            return true
        }

        return false
    }

    fun getNode(element: T): Node<T>? {
        if (root?.content == element) return root

        val nodes = root?.getChilds()?.toMutableList()

        if (nodes != null) {
            while (nodes.isNotEmpty()) {
                val current = nodes.first()

                if (current.content?.equals(element) == true) {
                    return current
                } else {
                    nodes.addAll(current.getChilds())
                    nodes.removeAt(0)
                }
            }
        }

        return null
    }

    private fun tryReinsert() {
        var changed = false
        var current: Pair<T, T>
        var i = 0

        while (i in orphans.indices) {
            current = orphans[i]

            if (putToParent(current.first, current.second)) {
                orphans.removeAt(i)
                changed = true
            } else {
                ++i
            }
        }

        if (changed) tryReinsert()
    }

    class Node<U>(val content: U, child: U? = null) {
        private val children = LinkedList<Node<U>>()

        init {
            if (child != null) {
                children.add(Node(child))
            }
        }

        fun addChild(child: Node<U>) = children.add(child)

        fun getChilds(): List<Node<U>> = children.toList()
    }
}

class Graph<T> {
    private val adjacencyMap: HashMap<T, HashSet<T>> = HashMap()

    fun addEdge (sourceVertex: T, destinationVertex: T) {
        adjacencyMap.computeIfAbsent(sourceVertex) { HashSet() }.add(destinationVertex)
        adjacencyMap.computeIfAbsent(destinationVertex) { HashSet() }.add(sourceVertex)
    }

    operator fun get (sourceVertex: T) : HashSet<T>? = adjacencyMap[sourceVertex]

    override fun toString(): String = StringBuffer().apply {
        for (key in adjacencyMap.keys) {
            append("$key -> ")
            append(adjacencyMap[key]?.joinToString(",", "[", "]\n"))
        }
    }.toString()
}
