package guessing

import base_project.GuessResult
import java.time.Duration
import java.time.Instant

// Пример дженерика + итератора
class History<T> : Iterable<T> {
    private val items = mutableListOf<T>()

    fun add(item: T) { items += item }
    fun toList(): List<T> = items.toList()

    override fun iterator(): Iterator<T> = items.iterator()
}

class StatsTracker {
    private var sessionStart: Instant = Instant.now()
    private var roundStart: Instant = Instant.now()

    private var roundsPlayed: Int = 0
    private var bestAttempts: Int? = null

    private val attemptsPerRound = History<Int>()

    fun onGuess(result: GuessResult) {
        // Можно считать "шумную" статистику, напр., дельту последнего сравнения
        // но для базового модуля достаточно реакции на Correct в onRoundFinished
    }

    fun onRoundFinished(attempts: Int) {
        attemptsPerRound.add(attempts)
        roundsPlayed += 1
        bestAttempts = when (bestAttempts) {
            null -> attempts
            else -> minOf(bestAttempts!!, attempts)
        }
    }

    fun startNewRound() {
        roundStart = Instant.now()
    }

    fun formatSession(): String {
        val elapsed = Duration.between(sessionStart, Instant.now())
        val attemptsText = attemptsPerRound.toList().joinToString()
        return """
            Сессия: ${elapsed.toSeconds()} сек
            Раундов сыграно: $roundsPlayed
            Попытки по раундам: $attemptsText
            Лучший результат (меньше — лучше): ${bestAttempts ?: "-"}
        """.trimIndent()
    }

    fun finalizeAndFormat(): String {
        return formatSession()
    }
}
