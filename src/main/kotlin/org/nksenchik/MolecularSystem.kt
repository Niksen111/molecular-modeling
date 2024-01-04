package org.nksenchik

import org.nksenchik.vo.Vector3D
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class MolecularSystem(
    val moleculesNumber: Int = 10,
    T: Double = 293.0,
    val sigma: Double = 3.54,
    val epsilon: Double = 0.00801,
    val m0: Double = 6.6335209e-26,
    cubeSize: Double = 1000.0
) {
    val T: Double
    val randomSeed = 42
    val molecules: List<Molecule>
    val cubeSize: Double
    val activeMolecules: MutableList<Boolean> = MutableList(moleculesNumber) { true }
    private val maxwell: (Double) -> Double
    private val vNaiver: Double
    private val random: Random

    init {
        require(moleculesNumber > 0)
        this.T = T * (kB / epsilon)
        this.maxwell = { v: Double ->
            4 * PI * (1.0 / (2 * PI * kB * this.T)).pow(1.5) * v * v * exp(-(v * v / (2 * kB * this.T)))
        }
        this.vNaiver = sqrt(2 * this.T * kB)
        this.random = Random(randomSeed)
        this.cubeSize = cubeSize / sigma
        this.molecules = setMolecules()
    }

    fun performSimulation(stepsNumber: Int, dt: Double = 10.0e-12): List<List<Vector3D>> {
        val tau = sigma * sqrt(m0 / epsilon)
        val dtScaled = dt / tau
        val history = mutableListOf(molecules.map { it.coordinates })
        for (step in 0..<stepsNumber) {
            updateSystem(dtScaled)
            history.add(molecules.map { it.coordinates })
        }

        return history
    }

    private fun calculateForce(firstCoords: Vector3D, secondCoords: Vector3D): Vector3D {
        val distance = firstCoords.distance(secondCoords)
        val coefficient = -24.0 * sigma * (distance.pow(-7) - 2 * distance.pow(-13)) / distance
        return (firstCoords - secondCoords).scale(coefficient)
    }

    private fun setMolecules(): List<Molecule> {
        val coords = generateCoordsList()
        val speedVectors = generateSpeedList()
        val accelerations = generateAccelerations()

        return speedVectors.mapIndexed {index, speedVector ->
            Molecule(coords[index], speedVector, accelerations[index])
        }
    }

    private fun updateAcceleration(molecule: Molecule) {
        molecule.a += calculateForce(molecule.coordinates, Vector3D(cubeSize, molecule.getY(), molecule.getZ()))
    }

    private fun updateAccelerations() {
        molecules.forEach { molecule ->
            updateAcceleration(molecule)
        }
    }

    private fun updateVelocity(molecule: Molecule, dt: Double) {
        molecule.v += molecule.a.scale(dt)
    }

    private fun updateVelocities(dt: Double) {
        molecules.forEach { molecule ->
            updateVelocity(molecule, dt)
        }
    }

    private fun updateCoordinates(molecule: Molecule, index: Int, dt: Double) {
        molecule.coordinates += molecule.v.scale(dt) + molecule.a.scale(dt.pow(2) / 2)
        molecule.coordinates = Vector3D(molecule.getX(), molecule.getY().mod(cubeSize), molecule.getZ().mod(cubeSize))

        if (molecule.getX() > cubeSize) {
            molecule.coordinates = Vector3D(cubeSize - molecule.getX(), molecule.getY(), molecule.getZ())
            molecule.v = molecule.v.scale(-1.0)
        }

        if (molecule.getX() < 0) {
            activeMolecules[index] = false
            molecule.coordinates = Vector3D(0, molecule.getY(), molecule.getZ())
        }
    }

    private fun updateSystemCoordinates(dt: Double) {
        molecules.forEachIndexed { index, molecule ->
            updateCoordinates(molecule, index, dt)
        }
    }

    private fun updateSystem(dt: Double) {
        updateSystemCoordinates(dt)
        updateVelocities(dt)
        updateAccelerations()
    }

    private fun generateAccelerations(): List<Vector3D> {
        return List(moleculesNumber) {
            Vector3D(0, 0, 0)
        }
    }

    private fun generateSpeedList(): List<Vector3D> {
        val bound = vNaiver * 3
        val h = bound / this.moleculesNumber
        val segments = mutableListOf<Pair<Double, Double>>()
        val moleculesCounts = mutableListOf<Int>()
        val countsAndIndexes = mutableListOf<Pair<Int, Double>>()
        var ceil = 0

        for (i in 0..<this.moleculesNumber) {
            segments.add(Pair(i * h, (i + 1) * h))
            moleculesCounts.add((maxwell(i * h) * h * moleculesNumber).toInt())
            ceil += moleculesCounts[i]
            countsAndIndexes.add(Pair(i, maxwell(i * h) * h * moleculesNumber - moleculesCounts[i]))
        }

        countsAndIndexes.sortByDescending { it.second }
        countsAndIndexes.take(moleculesNumber - ceil).forEach { moleculesCounts[it.first] += 1 }

        val speedList = mutableListOf<Vector3D>()
        moleculesCounts.forEachIndexed { index, count ->
            for (i in 0..<count) {
                speedList.add(generateSpeedVector(random.nextDouble(segments[index].first, segments[index].second)))
            }
        }

        speedList.shuffle(random)
        return speedList
    }

    private fun generateCoordsList(): List<Vector3D> {
        return List(moleculesNumber) {
            Vector3D(0, random.nextDouble(0.0, cubeSize), random.nextDouble(0.0, cubeSize))
        }
    }

    private fun generateSpeedVector(v: Double): Vector3D {
        val speed = arrayOf(0.0, 0.0, 0.0)
        speed[0] = random.nextDouble(v)
        speed[1] = random.nextDouble(sqrt(v.pow(2) - speed[0].pow(2)))
        speed[2] = sqrt(v.pow(2) - speed[0].pow(2) - speed[1].pow(2))
        speed.shuffle(random)
        return Vector3D(
            speed[0],
            speed[1] * arrayOf(-1, 1).random(random),
            speed[2] * arrayOf(-1, 1).random(random)
        )
    }

    companion object {
        const val kB = 8.617333262e-5
    }
}