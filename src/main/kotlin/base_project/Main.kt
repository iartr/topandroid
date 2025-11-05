package base_project

import guessing.StatsTracker
import kotlin.system.exitProcess

private const val APP_NAME: String = "Guess 0..100"

fun main() {
    println("=== $APP_NAME ===")
    println("Команды: help | exit | stats")

    // --- Выбор сложности ---
    val difficultyLevel = askDifficulty() // "easy", "normal", "hard"
    val strategy = createDifficultyStrategy(difficultyLevel)

    // --- Конфигурация игры ---
    val configMin = strategy.getRange().first
    val configMax = strategy.getRange().last
    val maxAttempts = strategy.getMaxAttempts()
    val allowHints = strategy.getHintPenalty() == 0

    val config = GameConfig(
        min = strategy.getRange().first,
        max = strategy.getRange().last,
        maxAttempts = strategy.getMaxAttempts(),
        allowHints = strategy.getHintPenalty() == 0
    )


    // Генератор случайного числа
    val randomProvider: RandomProvider = KotlinRandomProvider()

    // Игровой движок
    val engine = GameEngine(config, randomProvider)

    // Статистика
    val stats = StatsTracker()

    gameLoop@ while (true) {
        print("Введите число [${config.min}..${config.max}] или команду: ")
        val line = readlnOrNull()?.trim()

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
            line.isBlank() -> continue@gameLoop
            else -> {
                val guess: Int? = InputValidator.tryParseInt(line)
                if (guess == null) {
                    println("Введите, пожалуйста, целое число.")
                    continue@gameLoop
                }

                val result = engine.evaluateGuess(guess)
                stats.onGuess(result)

                println(Feedback.format(result, engine.remainingAttemptsOrNull()))

                // Если угадали число
                if (result is GuessResult.Correct) {
                    println("Секретное число: ${engine.revealSecret()} (угадано за ${result.attempts} попыток)")
                    stats.onRoundFinished(result.attempts)

                    val playAgain = askYesNo("Сыграть ещё? (y/n): ")
                    if (playAgain) {
                        engine.reset()
                        stats.startNewRound()
                    } else break@gameLoop
                }
                // Если достигнут лимит попыток
                else if (config.maxAttempts != null && engine.remainingAttemptsOrNull() == 0) {
                    println("Попытки закончились! Секретное число было: ${engine.revealSecret()}")
                    stats.onRoundFinished(engine.historySnapshot().size)
                    val playAgain = askYesNo("Сыграть ещё? (y/n): ")
                    if (playAgain) {
                        engine.reset()
                        stats.startNewRound()
                    } else break@gameLoop
                }
            }
        }
    }

    println("\n=== Итоги ===")
    println(stats.finalizeAndFormat())
    exitProcess(0)
}

private fun askDifficulty(): String {
    while (true) {
        println("Выберите сложность: 1) EASY  2) NORMAL  3) HARD")
        when (readlnOrNull()?.trim()?.lowercase()) {
            "1", "easy" -> return "easy"
            "2", "normal" -> return "normal"
            "3", "hard" -> return "hard"
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
            askYesNo(prompt)
        }
    }
}

private fun helpText(config: Any): String = """
    Правила:
    — Я загадываю целое число.
    — Вводите число, а я отвечаю: больше/меньше/угадал.
    — Команды: help, stats, exit.
""".trimIndent()
