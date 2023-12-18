package org.nksenchik

import org.nksenchik.vo.SpeedVector

data class Molecule(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0,
    var v: SpeedVector = SpeedVector(0, 0, 0)
) {

    override fun toString(): String {
        return "x=$x y=$y z=$z v=$v"
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
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

        return !(o.x != this.x || o.y != this.y || o.z != this.z || o.v != this.v)
    }
}