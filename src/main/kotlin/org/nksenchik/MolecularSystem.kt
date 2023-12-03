package org.nksenchik

class MolecularSystem(
    moleculesNumber: Int = 10
) {
    val molecules: List<Molecule> = emptyList()

    init {
        require(moleculesNumber > 0)

    }

    private fun setMolecules() {
        TODO()
    }
}