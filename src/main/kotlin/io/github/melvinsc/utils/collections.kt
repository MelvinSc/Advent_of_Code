package io.github.melvinsc.utils

import java.util.*

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