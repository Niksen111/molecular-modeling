package org.nksenchik.vo

import kotlin.math.pow
import kotlin.math.sqrt

data class Point3D(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0
) {
    fun setCoordinates(x: Double = this.x, y: Double = this.y, z: Double = this.z) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun setCoordinates(point: Point3D) {
        setCoordinates(point.x, point.y, point.z)
    }

    fun add(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) {
        this.x += x
        this.y += y
        this.z += z
    }

    fun add(point: Point3D) {
        add(point.x, point.y, point.z)
    }

    fun distance(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Double {
        return sqrt((this.x - x).pow(2) + (this.y - y).pow(2) + (this.z - z).pow(2))
    }

    fun distance(point: Point3D): Double = distance(point.x, point.y, point.z)

    fun dotProduct(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Double {
        return this.x * x + this.y * y + this.z * z
    }

    fun dotProduct(point: Point3D): Double = dotProduct(point.x, point.y, point.z)

    fun midpoint(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Point3D {
        return Point3D((this.x + x) / 2, (this.y + y) / 2, (this.z + z) / 2)
    }

    fun midpoint(point: Point3D): Point3D = midpoint(point.x, point.y, point.z)

    companion object {
        val ZERO: Point3D = Point3D()
    }
}