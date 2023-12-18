package org.nksenchik.vo

import kotlin.math.sqrt

data class SpeedVector(
    val x: Double,
    val y: Double,
    val z: Double
) {
    constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())

    val sqr = x*x + y*y + z*z
    val mod = sqrt(sqr)

    override fun toString(): String {
        return "[$x;$y;$z]"
    }
}
