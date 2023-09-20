package io.github.melvinsc.utils

fun reorientate(matrix: Array<CharArray>, orientation: Int): Array<CharArray> {
    var ret = if (orientation >= 4) {
        flipVertical(matrix)
    } else {
        copy(matrix)
    }

    for (i in 0 until orientation % 4) {
        var tmp = copy(ret)
        tmp = transpose(tmp)
        tmp = flipHorizontal(tmp)
        ret = tmp
    }

    return ret
}

fun transpose(matrix: Array<CharArray>): Array<CharArray> {
    val ret = Array(matrix.first().size) { CharArray(matrix.size) }

    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            ret[i][j] = matrix[j][i]
        }
    }

    return ret
}

fun copy(matrix: Array<CharArray>): Array<CharArray> {
    val ret = Array(matrix.first().size) { CharArray(matrix.size) }

    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            ret[i][j] = matrix[i][j]
        }
    }

    return ret
}

fun flipHorizontal(matrix: Array<CharArray>): Array<CharArray> {
    val ret = Array(matrix.size) { CharArray(matrix[it].size) }

    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            ret[ret.size - 1 - i][j] = matrix[i][j]
        }
    }

    return ret
}

fun flipVertical(matrix: Array<CharArray>): Array<CharArray> {
    val ret = Array(matrix.size) { CharArray(matrix[it].size) }

    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            ret[i][ret[i].size - 1 - j] = matrix[i][j]
        }
    }

    return ret
}

fun ByteArray.countLeadingZeroBits(): Int {
    val index = this.indexOfFirst { it.countLeadingZeroBits() != 8 }
    return index * 8 + this[index].countLeadingZeroBits()
}
