package io.github.melvinsc.utils

enum class Directions(val step: Vector2D) {
    U(0, -1),
    D(0, 1),
    L(-1, 0),
    R(1, 0),
    UL(-1, -1),
    UR(1, -1),
    DL(-1, 1),
    DR(1, 1);

    constructor(x: Int, y: Int) : this(Vector2D(x, y))
}