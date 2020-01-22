package io.github.melvinsc.utils

import kotlin.math.sign

data class Vector2D(var x: Int = 0, var y: Int = 0) {
    operator fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)

    operator fun minus(other: Vector2D) = Vector2D(x - other.x, y - other.y)

    operator fun times(other: Int) = Vector2D(x * other, y * other)

    fun lengthSquared() = x * x + y * y

    fun direction(other: Vector2D): Vector2D {
        val diff = other - this
        return when {
            diff.x == 0 -> Vector2D(0, diff.y.sign)
            diff.y == 0 -> Vector2D(diff.x.sign, 0)
            else -> {
                val gcd = gcd(diff.x, diff.y)
                Vector2D(diff.x / gcd, diff.y / gcd)
            }
        }
    }
}

data class Vector3D(var x: Int = 0, var y: Int = 0, var z: Int = 0) {
    operator fun plus(other: Vector3D) = Vector3D(x + other.x, y + other.y, z + other.z)
    operator fun plusAssign(other: Vector3D) = kotlin.run { x += other.x; y += other.y; z += other.z }
}