package org.nksenchik.vo

import kotlin.math.sqrt

data class SpeedVector(
    val x: Double,
    val y: Double
) {
    constructor(x: Number, y: Number) : this(x.toDouble(), y.toDouble())

    val mod = sqrt(x*x + y*y)

    override fun toString(): String {
        return "[$x;$y]"
    }
}
