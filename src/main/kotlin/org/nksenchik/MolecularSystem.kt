package org.nksenchik

import org.nksenchik.vo.History
import org.nksenchik.vo.MoleculeToDraw
import org.nksenchik.vo.Vector3D
import java.io.File
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
    cubeSize: Double = 2 * sqrt(sigma / PI) * 50,
    u0: Double = 0.3
) {
    val T: Double
    val randomSeed = 2
    val molecules: List<Molecule>
    val cubeSize: Double
    val activeMolecules: MutableList<Boolean> = MutableList(moleculesNumber) { true }
    val m: Double
    val u: Double
    private val maxwell: (Double) -> Double
    private val vNaiver: Double
    private val random: Random
    private var active = moleculesNumber
    private var calculateForces = false

    init {
        require(moleculesNumber > 0)
        this.T = T * (kB / epsilon)
        this.m = this.m0 / m0
        this.vNaiver = sqrt(2 * this.T * kB)
        this.random = Random(randomSeed)
        this.cubeSize = cubeSize / sigma
        this.u = u0 * (sqrt(m0 / epsilon))
        this.maxwell = { v: Double ->
            4 * PI * (m / (2 * PI * kB * this.T)).pow(1.5) * (v - u).pow(2) * exp(-(m * (v - u).pow(2) / (2 * kB * this.T)))
        }
        this.molecules = setMolecules()
    }

    fun drawTrajectories(history: List<List<History>>) {
        val moleculesToDraw = history.flatMapIndexed { i, histories ->
            histories.mapIndexed { j, history ->
                MoleculeToDraw(j, i, history.coordinates.x, history.coordinates.y, history.coordinates.z)
            }
        }

        val csv = csvOf(listOf("ID", "STEP", "X", "Y", "Z"),
            moleculesToDraw
        ) { molecule: MoleculeToDraw ->
            listOf(molecule.id, molecule.step, molecule.x, molecule.y, molecule.z).map { it.toString() }
        }

        val file = File("src/main/resources/data.csv")
        file.writeText(csv)
        ProcessBuilder("python", "src/main/resources/molecules_drawer.py").start()
    }

    private fun <T> csvOf(
        headers: List<String>,
        data: List<T>,
        itemBuilder: (T) -> List<String>
    ) = buildString {
        append(headers.joinToString(",") { "\"$it\"" })
        append("\n")
        data.forEach { item ->
            append(itemBuilder(item).joinToString(",") { "\"$it\"" })
            append("\n")
        }
    }

    fun performSimulation(stepsNumber: Int, dt: Double = 10.0e-12): List<List<History>> {
        val tau = sigma * sqrt(m0 / epsilon)
        val dtScaled = dt / tau
        val history = mutableListOf(molecules.map { History(it) })
        for (step in 0..<stepsNumber) {
            updateSystem(dtScaled)
            history.add(molecules.map { History(it) })
        }

        return history
    }

    private fun calculateForce(firstCoords: Vector3D, secondCoords: Vector3D): Vector3D {
        val distance = firstCoords.distance(secondCoords)
        val coefficient = 24.0 * epsilon * (2 * distance.pow(-14) - distance.pow(-8))
        return if (calculateForces) {
            (firstCoords - secondCoords).scale(coefficient)
        } else {
            Vector3D.ZERO
        }
    }

    private fun setMolecules(): List<Molecule> {
        val coords = generateCoordsList()
        val speedVectors = generateSpeedList()
        val accelerations = generateAccelerations(coords)

        return coords.mapIndexed {index, coord ->
            Molecule(coord, speedVectors[index], accelerations[index])
        }
    }

    private fun updateAcceleration(molecule: Molecule) {
        molecule.a = calculateForce(molecule.coordinates, Vector3D(cubeSize, molecule.getY(), molecule.getZ()))
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
            molecule.coordinates = Vector3D(cubeSize - (molecule.getX() - cubeSize), molecule.getY(), molecule.getZ())
            molecule.v = Vector3D(-molecule.v.x, molecule.v.y, molecule.v.z)
        }

        if (molecule.getX() < 0) {
            activeMolecules[index] = false
            active--
            molecule.coordinates = Vector3D(-1, molecule.getY(), molecule.getZ())
        }
    }

    private fun updateSystemCoordinates(dt: Double) {
        if (active == 0) {
            return
        }

        molecules.forEachIndexed { index, molecule ->
            if (activeMolecules[index]) {
                updateCoordinates(molecule, index, dt)
            }
        }
    }

    private fun updateSystem(dt: Double) {
        updateSystemCoordinates(dt)
        updateVelocities(dt)
        updateAccelerations()
    }

    private fun generateAccelerations(coordinates: List<Vector3D>): List<Vector3D> {
        return coordinates.map { calculateForce(it, Vector3D(cubeSize, it.y, it.z)) }
    }

    private fun generateSpeedList(): List<Vector3D> {
        val bound = vNaiver * 3
        val h = bound / this.moleculesNumber
        val segments = mutableListOf<Pair<Double, Double>>()
        val moleculesCounts = mutableListOf<Int>()
        val countsAndIndexes = mutableListOf<Pair<Int, Double>>()
        var ceil = 0

        for (i in 0..<this.moleculesNumber) {
            segments.add(Pair(u + i * h, u + (i + 1) * h))
            moleculesCounts.add((maxwell(u + i * h) * h * moleculesNumber).toInt())
            ceil += moleculesCounts[i]
            countsAndIndexes.add(Pair(i, maxwell(u + i * h) * h * moleculesNumber - moleculesCounts[i]))
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