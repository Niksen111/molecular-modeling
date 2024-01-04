package org.nksenchik

import org.nksenchik.vo.Vector3D

class Molecule(
    var coordinates: Vector3D = Vector3D(),
    var v: Vector3D = Vector3D(0, 0, 0),
    var a: Vector3D = Vector3D(0, 0, 0)
) {
    fun distance(molecule: Molecule) = coordinates.distance(molecule.coordinates)

    fun getX() = coordinates.x
    fun getY() = coordinates.y
    fun getZ() = coordinates.z

    override fun toString(): String {
        return "x=${coordinates.x} y=${coordinates.y} z=${coordinates.z} v=$v a=$a"
    }

    override fun hashCode(): Int {
        var result = coordinates.hashCode()
        result = 31 * result + v.hashCode()
        result = 31 * result + a.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (this === other) {
            return false
        }

        if (other !is Molecule) {
            return false
        }

        if (other.hashCode() != this.hashCode()) {
            return false
        }

        val o: Molecule = other

        return !(o.coordinates != this.coordinates || o.v != this.v || o.a != this.a)
    }
}