package io.github.melvinsc.year2020.day18

class AdvancedEvaluator(private val str: String) {

    private var position = -1
    private var currentChar = '#'

    fun evaluate(): Long {
        setNextChar()
        val current = parseMultiplication()

        if (position < str.length) {
            throw RuntimeException("Unexpected: $currentChar")
        } else {
            return current
        }
    }

    private fun setNextChar() {
        currentChar = if (++position < str.length) {
            str[position]
        } else {
            '#'
        }
    }

    private fun isCurrentChar(charToEval: Char) = if (currentChar == charToEval) {
        setNextChar()
        true
    } else {
        false
    }

    private fun parseMultiplication(): Long {
        var current = parseAddition()
        while (true) {
            if (isCurrentChar('*')) {
                current *= parseAddition()
            } else {
                return current
            }
        }
    }

    private fun parseAddition(): Long {
        var current = parseNext()

        while (true) {
            if (isCurrentChar('+')) {
                current += parseNext()
            } else {
                return current
            }
        }
    }

    private fun parseNext(): Long = if (isCurrentChar('+')) {
        parseNext()
    } else {
        val current: Long

        when {
            isCurrentChar('(') -> {
                current = parseMultiplication()
                isCurrentChar(')')
            }
            currentChar.isDigit() -> {
                current = Character.getNumericValue(str[position]).toLong()
                setNextChar()
            }
            else -> {
                throw RuntimeException("Unexpected: $currentChar")
            }
        }

        current
    }
}