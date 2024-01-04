import org.nksenchik.MolecularSystem

fun main(args: Array<String>) {
    val system = MolecularSystem(3)
    val stepsNumber = 100000
    val history = system.performSimulation(stepsNumber, 1e-12)
    println(system.cubeSize)
    val filtered = history.filter { histories -> histories.any { it.coordinates.x > -1 } }
    filtered.filterIndexed { index, _ -> index % (filtered.size / 10) == 0  } .forEach {
        println(it)
    }
    system.drawTrajectories(filtered.filterIndexed { index, _ -> index % (filtered.size / 10) == 0  })
}