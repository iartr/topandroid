package base_project

interface DifficultyStrategy {
    fun getMaxAttempts(): Int
    fun getRange(): IntRange
    fun getHintPenalty(): Int
}