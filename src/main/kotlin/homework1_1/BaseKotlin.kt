package homework1_1

/*
* const val — compile-time константа
* Значение должно быть известно при компиляции. Только примитивные типы и String.
* Должна быть объявлена на top-level или в object/companion object
* Заменяется прямо в коде при компиляции (как #define в C)
* */
private const val MIN_VALUE = 0
private const val MAX_VALUE = 100

fun main() {
    println("Игра 0..100 — минималка")

    val secret = (MIN_VALUE..MAX_VALUE).random()

    while (true) {
        print("Введите число или exit: ")

        val line = readlnOrNull()?.trim() ?: return

        if (line.equals("exit", ignoreCase = true)) {
            return
        }

        if (line.equals("help", ignoreCase = true)) {
            println("Угадай число 0..100. Вводите целые. exit — выход.")
            continue
        }

        val guess = line.toIntOrNull()
        if (guess == null) {
            println("Введите целое число.")
            continue
        }

        when {
            guess < secret -> println("Моё число больше")
            guess > secret -> println("Моё число меньше")
            else -> {
                println("Угадали!")
                return
            }
        }
    }
}