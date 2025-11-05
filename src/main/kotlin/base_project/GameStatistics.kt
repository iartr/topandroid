package base_project

class GameStatistics(
    initialAttemptValue: Int = -1,
    historySize: Int = 50,
    hintsInitial: Int = 0
) {
    val startTime: Long by lazy { System.currentTimeMillis() }
    private val attemptsDelegate = HistoryDelegate(initialAttemptValue, historySize)
    var lastAttempt: Int by attemptsDelegate
        private set

    var hints: Int by RangeDelegate(hintsInitial, 0..10)

    fun addAttempt(value: Int) {
        lastAttempt = value
    }

    fun getAttemptsHistory(): List<Int> = attemptsDelegate.getHistory().filter { it >= 0 }

    fun generateReport(): String {
        val attempts = getAttemptsHistory()
        val totalAttempts = attempts.size
        val median = attempts.median()
        val mode = attempts.mode()
        val min = attempts.minOrNull()
        val max = attempts.maxOrNull()
        val sum = attempts.sumByType<Int>()
        val avg = if (totalAttempts > 0) sum / totalAttempts else 0.0
        val runtimeMs = System.currentTimeMillis() - startTime

        return buildString {
            appendLine("=== Game Statistics Report ===")
            appendLine("Runtime: $runtimeMs ms")
            appendLine("Total attempts: $totalAttempts")
            appendLine("Attempts: ${if (attempts.isEmpty()) "-" else attempts}")
            appendLine("Min: ${min ?: "-"}, Max: ${max ?: "-"}")
            appendLine("Median: ${median ?: "-"}, Mode: ${mode ?: "-"}")
            appendLine("Average: ${"%.2f".format(avg)}")
            appendLine("Hints used: $hints")
        }
    }
}
