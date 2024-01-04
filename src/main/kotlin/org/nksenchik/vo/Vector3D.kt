package org.nksenchik.vo

import kotlin.math.pow
import kotlin.math.sqrt

data class Vector3D(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0
) {
    constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())

    val sqr = x*x + y*y + z*z

    val mod = sqrt(sqr)

    operator fun unaryMinus() = Vector3D(-x, -y, -z)

    fun plus(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Vector3D {
        return Vector3D(this.x + x, this.y + y, this.z + z)
    }

    operator fun plus(point: Vector3D): Vector3D {
        return plus(point.x, point.y, point.z)
    }

    fun minus(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Vector3D {
        return Vector3D(this.x - x, this.y - y, this.z - z)
    }

    operator fun minus(point: Vector3D): Vector3D {
        return this + (-point)
    }

    fun scale(coefficient: Double): Vector3D {
        return Vector3D(x * coefficient, y * coefficient, z * coefficient)
    }

    fun distance(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Double {
        return sqrt((this.x - x).pow(2) + (this.y - y).pow(2) + (this.z - z).pow(2))
    }

    fun distance(point: Vector3D): Double = distance(point.x, point.y, point.z)

    fun dotProduct(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Double {
        return this.x * x + this.y * y + this.z * z
    }

    fun dotProduct(point: Vector3D): Double = dotProduct(point.x, point.y, point.z)

    fun midpoint(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Vector3D {
        return Vector3D((this.x + x) / 2, (this.y + y) / 2, (this.z + z) / 2)
    }

    fun midpoint(point: Vector3D): Vector3D = midpoint(point.x, point.y, point.z)

    companion object {
        val ZERO: Vector3D = Vector3D()
    }
}