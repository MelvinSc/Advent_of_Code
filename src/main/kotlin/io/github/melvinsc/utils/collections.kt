package io.github.melvinsc.utils

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