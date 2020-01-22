package io.github.melvinsc.year2019.day12

import io.github.melvinsc.utils.Vector3D
import io.github.melvinsc.utils.day.Day
import io.github.melvinsc.utils.lcm
import kotlin.math.abs
import kotlin.math.sign

fun main() = Day.setMain(Day12)

object Day12 : Day() {
    override fun first(inputData: String): Int {
        val moons = inputData.lines().map { line ->
            line.substring(1, line.length - 1).split(", ").map { coordinate ->
                coordinate.removeRange(0, 2).toInt()
            }
        }.map { Moon(it) }

        repeat(1000) {
            for (i in moons.indices) {
                for (j in moons.indices) {
                    if (i != j) {
                        moons[i].addVelocity(moons[j])
                    }
                }
            }
            moons.forEach { it.applyVelocity() }
        }

        return moons.sumBy { it.getTotalEnergy() }
    }

    override fun second(inputData: String): Long {
        fun initialStateIsReached(moons: List<Moon>, initialState: List<Moon>): Int {
            var x = 1
            var y = 2
            var z = 4

            for (i in moons.indices) {
                if (moons[i].pos.x != initialState[i].pos.x || moons[i].vel.x != initialState[i].vel.x) {
                    x = 0
                }
                if (moons[i].pos.y != initialState[i].pos.y || moons[i].vel.y != initialState[i].vel.y) {
                    y = 0
                }
                if (moons[i].pos.z != initialState[i].pos.z || moons[i].vel.z != initialState[i].vel.z) {
                    z = 0
                }
            }

            return x + y + z
        }

        val moons = inputData.lines().map { line ->
            line.substring(1, line.length - 1).split(", ").map { coordinate ->
                coordinate.removeRange(0, 2).toInt()
            }
        }.map { Moon(it) }

        val initialState = inputData.lines().map { line ->
            line.substring(1, line.length - 1).split(", ").map { coordinate ->
                coordinate.removeRange(0, 2).toInt()
            }
        }.map { Moon(it) }


        var iterations = 0L
        var xLoop = -1L
        var yLoop = -1L
        var zLoop = -1L

        do {
            for (i in moons.indices) {
                for (j in moons.indices) {
                    if (i != j) {
                        moons[i].addVelocity(moons[j])
                    }
                }
            }
            moons.forEach { it.applyVelocity() }
            ++iterations

            val loopsFound = initialStateIsReached(moons, initialState)
            if ((loopsFound and 1) != 0 && xLoop < 0) {
                xLoop = iterations
            }
            if ((loopsFound and 2) != 0 && yLoop < 0) {
                yLoop = iterations
            }
            if ((loopsFound and 4) != 0 && zLoop < 0) {
                zLoop = iterations
            }

            if (iterations % 10000000L == 0L) {
                println(iterations)
            }
        } while (xLoop < 0 || yLoop < 0 || zLoop < 0)

        return lcm(lcm(xLoop, yLoop), zLoop)
    }

    private data class Moon(var pos: Vector3D = Vector3D(), var vel: Vector3D = Vector3D()) {
        constructor(list: List<Int>) : this(Vector3D(list[0], list[1], list[2]))

        fun applyVelocity() = pos.plusAssign(vel)

        fun addVelocity(other: Moon) {
            val xVel = (other.pos.x - pos.x).sign
            val yVel = (other.pos.y - pos.y).sign
            val zVel = (other.pos.z - pos.z).sign
            vel.plusAssign(Vector3D(xVel, yVel, zVel))
        }

        fun getTotalEnergy() = pos.absSum() * vel.absSum()

        private fun Vector3D.absSum() = abs(this.x) + abs(this.y) + abs(this.z)
    }
}