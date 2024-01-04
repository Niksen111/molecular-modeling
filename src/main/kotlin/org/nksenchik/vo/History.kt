package org.nksenchik.vo

import org.nksenchik.Molecule

data class History(
    val coordinates: Vector3D,
    val v: Vector3D,
    val a: Vector3D
) {
    constructor(molecule: Molecule) : this(molecule.coordinates, molecule.v, molecule.a)
}
