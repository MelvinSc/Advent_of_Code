package io.github.melvinsc.utils

fun String.getEveryNth(interval: Int = 1, offset: Int = 0, length: Int = 1): String {
    val sb = StringBuilder()

    for (i in offset until this.length step interval) {
        sb.append(this.substring(i, i + length))
    }

    return sb.toString()
}

fun String.toBooleanArray(one: Char = '#'): BooleanArray {
    val ret = BooleanArray(this.length)

    for (i in this.indices) {
        if (this[i] == one) {
            ret[i] = true
        }
    }

    return ret
}