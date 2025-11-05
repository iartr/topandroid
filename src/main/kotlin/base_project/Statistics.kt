package guessing

import base_project.GuessResult
import base_project.GameStatistics

class StatsTracker {
    private val stats = GameStatistics(initialAttemptValue = -1, historySize = 50)
    private var roundsPlayed: Int = 0
    private var bestAttempts: Int? = null

    fun onGuess(result: GuessResult) {
        // можно записывать все попытки
    }

    fun onRoundFinished(attempts: Int) {
        stats.addAttempt(attempts)
        roundsPlayed += 1
        bestAttempts = bestAttempts?.let { minOf(it, attempts) } ?: attempts
    }

    fun startNewRound() {
        // сброс подсказок если нужно: stats.hints = 0
    }

    fun formatSession(): String =
        stats.generateReport() + "\nRounds played: $roundsPlayed\nBest attempts: ${bestAttempts ?: "-"}"

    fun finalizeAndFormat(): String = formatSession()
}
