package base_project

// Демонстрация модификаторов: класс открыт по умолчанию в Kotlin? Нет, закрыт.
// Мы явно не даем наследоваться (не ставим open) — инкапсулируем логику.
class GameEngine(
    private val config: GameConfig,
    private val randomProvider: RandomProvider
) {
    private var secret: Int = randomProvider.nextInt(config.min, config.max)
    private var attempts: Int = 0

    // История попыток — списки и итераторы
    private val history: MutableList<Int> = mutableListOf()

    fun evaluateGuess(guess: Int): GuessResult {
        // Проверка выхода за диапазон
        if (guess < config.min || guess > config.max) {
            return GuessResult.OutOfRange(config.min, config.max)
        }

        attempts += 1
        history += guess

        return when {
            guess < secret -> GuessResult.TooLow(secret - guess)
            guess > secret -> GuessResult.TooHigh(guess - secret)
            else -> GuessResult.Correct(attempts)
        }
    }

    fun remainingAttemptsOrNull(): Int? =
        config.maxAttempts?.let { it - attempts } // safe-call + let

    fun revealSecret(): Int = secret // пример геттера-обёртки

    fun reset() {
        secret = randomProvider.nextInt(config.min, config.max)
        attempts = 0
        history.clear()
    }

    // Пример: отдаём копию истории, не исходную (инкапсуляция)
    fun historySnapshot(): List<Int> = history.toList()
}
