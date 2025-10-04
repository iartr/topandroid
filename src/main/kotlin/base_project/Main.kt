package base_project

import guessing.StatsTracker
import kotlin.system.exitProcess

private const val APP_NAME: String = "Guess 0..100" // константа (тип можно не указывать — здесь показываем явно)

fun main() {
    println("=== $APP_NAME ===")
    println("Команды: help | exit | stats")

    // 1) Демонстрация enum + when: выбираем сложность
    val difficulty = askDifficulty()
    val config = GameConfig.fromDifficulty(difficulty)

    // 2) Демонстрация интерфейса и внедрения зависимости (DI) без фреймворка
    val randomProvider: RandomProvider = KotlinRandomProvider()

    // 3) Основной движок игры (инкапсулирует логику)
    val engine = GameEngine(config, randomProvider)

    // 4) Статистика (демонстрация работы со временем JVM + коллекции)
    val stats = StatsTracker()

    gameLoop@ while (true) {
        print("Введите число [${config.min}..${config.max}] или команду: ")
        val line = readlnOrNull()?.trim() // nullable + safe call + Elvis ниже

        when {
            line == null -> {
                println("EOF. Выход.")
                break@gameLoop
            }
            line.equals("exit", ignoreCase = true) -> {
                println("До встречи!")
                break@gameLoop
            }
            line.equals("help", ignoreCase = true) -> {
                println(helpText(config))
                continue@gameLoop
            }
            line.equals("stats", ignoreCase = true) -> {
                println(stats.formatSession())
                continue@gameLoop
            }
            line.isBlank() -> {
                // пустой ввод, просто продолжаем
                continue@gameLoop
            }
            else -> {
                // Парсим целое. Nullable возвращаемое значение тренирует работу с null.
                val guess: Int? = InputValidator.tryParseInt(line)
                if (guess == null) {
                    println("Введите, пожалуйста, целое число.")
                    continue@gameLoop
                }

                // Оценка попытки (sealed и when-ветвление)
                val result: GuessResult = engine.evaluateGuess(guess)
                stats.onGuess(result)

                // Форматирование ответа вынесено в отдельный модуль (лямбда внутри)
                println(Feedback.format(result, engine.remainingAttemptsOrNull()))

                // Условие победы
                if (result is GuessResult.Correct) {
                    println("Секретное число: ${engine.revealSecret()} (угадано за ${result.attempts} попыток)")
                    stats.onRoundFinished(result.attempts)

                    // Небольшая демонстрация if/else как выражений
                    val playAgain = askYesNo("Сыграть ещё? (y/n): ")
                    if (playAgain) {
                        engine.reset()
                        stats.startNewRound()
                    } else {
                        break@gameLoop
                    }
                }
            }
        }
    }

    // Завершение сессии
    println()
    println("=== Итоги ===")
    println(stats.finalizeAndFormat())
    exitProcess(0)
}

private fun askDifficulty(): Difficulty {
    while (true) {
        println("Выберите сложность: 1) EASY  2) NORMAL  3) HARD")
        when (readlnOrNull()?.trim()) {
            "1", "easy", "EASY" -> return Difficulty.EASY
            "2", "normal", "NORMAL" -> return Difficulty.NORMAL
            "3", "hard", "HARD" -> return Difficulty.HARD
            else -> println("Не понял. Введите 1, 2 или 3.")
        }
    }
}

private fun askYesNo(prompt: String): Boolean {
    print(prompt)
    return when (readlnOrNull()?.trim()?.lowercase()) {
        "y", "yes", "да", "д" -> true
        "n", "no", "нет", "н" -> false
        else -> {
            println("Введите y/n.")
            askYesNo(prompt) // рекурсия допустима, но можно и цикл
        }
    }
}

private fun helpText(config: GameConfig): String = """
    Правила:
    — Я загадываю целое число в диапазоне ${config.min}..${config.max}.
    — Вводите число, а я отвечаю: больше/меньше/угадал.
    — Команды: help, stats, exit.
""".trimIndent()
