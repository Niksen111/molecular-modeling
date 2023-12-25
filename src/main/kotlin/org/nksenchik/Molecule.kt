package org.nksenchik

import org.nksenchik.vo.Point3D
import org.nksenchik.vo.SpeedVector

class Molecule(
    var coordinates: Point3D = Point3D(),
    var v: SpeedVector = SpeedVector(0, 0, 0)
) {

    override fun toString(): String {
        return "x=${coordinates.x} y=${coordinates.y} z=${coordinates.z} v=$v"
    }

    override fun hashCode(): Int {
        var result = coordinates.hashCode()
        result = 31 * result + v.hashCode()
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

        return !(o.coordinates != this.coordinates || o.v != this.v)
    }
}