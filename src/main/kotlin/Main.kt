import org.nksenchik.MolecularSystem
import org.nksenchik.vo.History
import java.lang.Exception
import kotlin.math.PI
import kotlin.math.sqrt

fun printInfo() {
    println("Программа для моделирования движения молекул и отражения их от зеркальной поверхности.")
}

fun printCommands() {
    println("Список команд:")
    println("помощь                 выводит список команд на экран")
    println("вводПарам              позволяет ввести параметры системы")
    println("печатьПарам            печатает параметры на экран")
    println("старт                  запускает симуляцию и рисует траектории")
    println("выход                  выйти из программы")
}

fun printErr() {
    println("ОШИБКА: Введена неверная команда")
}


fun main(args: Array<String>) {
    var moleculesNumber = 3
    var stepsNumber = 1000
    var dt = 1e-10
    var T = 293.0
    var sigma = 3.54
    var epsilon = 0.00801
    var m0 = 6.6335209e-26
    var cubeSize: Double = 2 * sqrt(sigma / PI) * 50
    printInfo()
    printCommands()
    while (true) try {
        println("(введите 'помощь' чтобы увидеть список команд)")
        println("Введите команду:")
        val command = readln().split(' ')
        if (command.size != 1) {
            printErr()
            continue
        }

        when (command[0]) {
            "помощь" -> {
                printCommands()
            }
            "вводПарам" -> {
                println("Нажмите Enter чтобы оставить текущее значение")
                println("Введите количество молекул в системе (текущее $moleculesNumber):")
                moleculesNumber = readln().toIntOrNull() ?: moleculesNumber
                println("Введите количество шагов симуляции (текущее $stepsNumber):")
                stepsNumber = readln().toIntOrNull() ?: stepsNumber
                println("Введите dt (временной шаг симуляции) (текущее $dt):")
                dt = readln().toDoubleOrNull() ?: dt
                println("Введите температру системы (текущее $T):")
                T = readln().toDoubleOrNull() ?: T
                println("Введите значение сигма (текущее $sigma):")
                sigma = readln().toDoubleOrNull() ?: sigma
                println("Введите значение эпсилон (текущее $epsilon):")
                epsilon = readln().toDoubleOrNull() ?: epsilon
                println("Введите массу молекулы (текущее $m0):")
                m0 = readln().toDoubleOrNull() ?: m0
                println("Введите размер куба (текущее $cubeSize):")
                cubeSize = readln().toDoubleOrNull() ?: cubeSize
            }
            "печатьПарам" -> {
                println("Количество молекул в системе: $moleculesNumber")
                println("Количество шагов симуляции: $stepsNumber")
                println("dt (временной шаг симуляции): $dt")
                println("Температра системы: $T")
                println("Значение сигма: $sigma")
                println("Значение эпсилон: $epsilon")
                println("Масса молекулы: $m0")
                println("Размер куба: $cubeSize")
            }
            "старт" -> {
                val system = MolecularSystem(moleculesNumber, T, sigma, epsilon, m0, cubeSize)
                val history = system.performSimulation(stepsNumber, dt)
                val filtered = history.filter { histories -> histories.any { it.coordinates.x > -1 } }
                system.drawTrajectories(filtered)
            }
            "выход" -> {
                break
            }
            else -> {
                printErr()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}