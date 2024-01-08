import org.nksenchik.MolecularSystem
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
    var T: Double = 293.0
    var sigma: Double = 3.54
    var epsilon: Double = 0.00801
    var m0: Double = 6.6335209e-26
    var cubeSize: Double = 2 * sqrt(sigma / PI) * 50
    var system = MolecularSystem(moleculesNumber)
    var history = system.performSimulation(stepsNumber, dt)
    var filtered = history.filter { histories -> histories.any { it.coordinates.x > -1 } }
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
                println("Нажмите Enter чтобы оставить значение по умолчанию")
                println("Введите количество молекул в системе (по умолч. 3):")
                moleculesNumber = readln().toIntOrNull() ?: 3
                println("Введите количество шагов симуляции (по умолч. 1000):")
                stepsNumber = readln().toIntOrNull() ?: 1000
                println("Введите dt (временной шаг симуляции) (по умоч. 1e-10):")
                dt = readln().toDoubleOrNull() ?: 1e-10
                println("Введите температру системы (по умолч. 293.0):")
                T = readln().toDoubleOrNull() ?: 293.0
                println("Введите значение сигма (по умолч. 3.54):")
                sigma = readln().toDoubleOrNull() ?: 3.54
                println("Введите значение эпсилон (по умолч. 0.00801):")
                epsilon = readln().toDoubleOrNull() ?: 0.00801
                println("Введите массу молекулы (по умолч. 6.6335209e-26):")
                m0 = readln().toDoubleOrNull() ?: 6.6335209e-26
                println("Введите размер куба (по умолч. ${2 * sqrt(sigma / PI) * 50}):")
                cubeSize = readln().toDoubleOrNull() ?: (2 * sqrt(sigma / PI) * 50)
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
                system = MolecularSystem(moleculesNumber, T, sigma, epsilon, m0, cubeSize)
                history = system.performSimulation(stepsNumber, dt)
                filtered = history.filter { histories -> histories.any { it.coordinates.x > -1 } }
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