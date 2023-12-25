package org.nksenchik

import org.nksenchik.vo.Point3D
import org.nksenchik.vo.SpeedVector
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class MolecularSystem(
    val moleculesNumber: Int = 10,
    val moleculeRadius: Double = 1.0,
    val mu: Double = 39.948,
    val T: Double = 300.0
) {
    private val molecules: List<Molecule> = setMolecules()
    private val maxwell: (Double) -> Double = { v: Double ->
        4 * PI * (mu / (2 * PI * R * T)).pow(1.5) * v * v * exp(- (mu * v * v / (2 * R * T)))
    }
    private val boltzmann: (Double) -> Double = { h: Double ->
        1.0
    }
    private val vNaiver = sqrt(2 * T * R / mu)
    private val random = Random(System.currentTimeMillis())

    init {
        require(moleculesNumber > 0)
    }

    private fun setMolecules(): List<Molecule> {
        val speedVectors = generateSpeed()
        val coords = generateCoords()

        return speedVectors.mapIndexed {index, speedVector ->
            Molecule(coords[index], speedVector)
        }
    }

    private fun generateSpeed(): List<SpeedVector> {
        val bound = vNaiver * 3
        val h = bound / this.moleculesNumber
        val segments = mutableListOf<Pair<Double, Double>>()
        val moleculesCounts = mutableListOf<Int>()
        val countsAndIndexes = mutableListOf<Pair<Int, Double>>()
        var ceil = 0

        for (i in 0..<this.moleculesNumber) {
            segments.add(Pair(i * h, (i + 1) * h))
            moleculesCounts.add((maxwell(i * h) * h).toInt())
            ceil += moleculesCounts[i]
            countsAndIndexes.add(Pair(i, maxwell(i * h) * h - moleculesCounts[i]))
        }

        countsAndIndexes.sortBy { it.second }
        countsAndIndexes.take(moleculesNumber - ceil)
        countsAndIndexes.forEach { moleculesCounts[it.first] += 1 }

        val speedList = mutableListOf<SpeedVector>()
        moleculesCounts.forEachIndexed { index, count ->
            for (i in 0..<count) {
                speedList.add(generateSpeedVector(random.nextDouble(segments[index].first, segments[index].second)))
            }
        }

        return speedList
    }

    private fun generateCoords(): List<Point3D> {
        return emptyList()
    }

    private fun generateSpeedVector(v: Double): SpeedVector {
        val speed = arrayOf(0.0, 0.0, 0.0)
        speed[0] = random.nextDouble(v)
        speed[1] = random.nextDouble(sqrt(v.pow(2) - speed[0].pow(2)))
        speed[2] = sqrt(v.pow(2) - speed[0].pow(2) - speed[1].pow(2))
        speed.shuffle(random)
        // TODO добавить '-' по нужным координатам
        return SpeedVector(speed[0], speed[1], speed[2])
    }

    companion object {
        const val R = 8.31446262
    }
}