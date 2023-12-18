package org.nksenchik

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

class MolecularSystem(
    moleculesNumber: Int = 10,
    moleculeRadius: Double = 1.0,
    val mu: Double = 39.948,
    val T: Double = 300.0
) {
    private val molecules: List<Molecule> = emptyList()
    private val maxwell3R: (Double) -> Double = { v: Double ->
        4 * PI * (mu / (2 * PI * R * T)).pow(1.5) * v * v * exp(- (mu * v * v / (2 * R * T)))
    }
    private val vNaiv = sqrt(2 * T * R / mu)

    init {
        require(moleculesNumber > 0)

    }

    private fun setMolecules() {
        // TODO()
    }

    companion object {
        const val R = 8.31446262
    }
}