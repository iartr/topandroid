package base_project

data class GameConfig(
    val min: Int,
    val max: Int,
    val maxAttempts: Int? = null,
    val allowHints: Boolean = false
)

fun createDifficultyStrategy(level: String): DifficultyStrategy {
    return when(level.lowercase().trim()) {
        "easy" -> object : DifficultyStrategy {
            override fun getMaxAttempts() = 15
            override fun getRange() = 1..50
            override fun getHintPenalty() = 0
        }
        "normal" -> object : DifficultyStrategy {
            override fun getMaxAttempts() = 10
            override fun getRange() = 1..100
            override fun getHintPenalty() = 1
        }
        "hard" -> object : DifficultyStrategy {
            override fun getMaxAttempts() = 7
            override fun getRange() = 1..200
            override fun getHintPenalty() = 2
        }
        else -> throw IllegalArgumentException("Unknown level: $level")
    }
}