import org.nksenchik.MolecularSystem

fun main(args: Array<String>) {
    val system = MolecularSystem(3)
    val stepsNumber = 1000
    val history = system.performSimulation(stepsNumber, 1e-10)
    val filtered = history.filter { histories -> histories.any { it.coordinates.x > -1 } }
    system.drawTrajectories(filtered)
}