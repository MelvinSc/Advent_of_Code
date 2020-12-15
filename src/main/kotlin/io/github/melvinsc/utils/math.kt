package io.github.melvinsc.utils

import kotlin.math.abs

fun gcd(a: Int, b: Int) = positiveGCD(abs(a), abs(b))

private fun positiveGCD(a: Int, b: Int): Int = if (b == 0) a else positiveGCD(b, a % b)

fun lcm(a: Long, b: Long): Long = abs(a * b) / gcd(a.toInt(), b.toInt())

fun exp(base: Int, exponent: Int): Int = when {
    base < 0 -> throw IllegalArgumentException("Base can't be less than 0")
    exponent < 0 -> throw IllegalArgumentException("Exponent can't be less than 0")
    exponent == 0 -> 1
    exponent == 1 -> base
    exponent % 2 == 0 -> exp(base * base, exponent / 2)
    else -> base * exp(base * base, (exponent - 1) / 2)
}