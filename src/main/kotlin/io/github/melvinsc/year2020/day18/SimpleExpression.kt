package io.github.melvinsc.year2020.day18

class SimpleExpression(private val str: String) {

    private var position = -1
    private var currentChar = '#'

    fun evaluate(): Long {
        setNextChar()
        val current = parseOperator()

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

    private fun parseOperator(): Long {
        var current = parseNext()

        while (true) {
            when {
                isCurrentChar('+') -> current += parseNext()
                isCurrentChar('*') -> current *= parseNext()
                else -> return current
            }
        }
    }

    private fun parseNext(): Long = if (isCurrentChar('+')) {
        parseNext()
    } else {
        val current: Long

        when {
            isCurrentChar('(') -> {
                current = parseOperator()
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