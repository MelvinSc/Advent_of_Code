package io.github.melvinsc.utils

import kotlin.math.abs

fun gcd(a: Int, b: Int) = positiveGCD(abs(a), abs(b))

private fun positiveGCD(a: Int, b: Int): Int = if (b == 0) a else positiveGCD(b, a % b)

fun lcm(a: Long, b: Long): Long = abs(a * b) / gcd(a.toInt(), b.toInt())