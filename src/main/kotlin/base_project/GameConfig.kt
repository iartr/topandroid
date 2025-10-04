package base_project

// data class — автоматические equals/hashCode/toString + неизменяемость по умолчанию (val)
data class GameConfig(
    val min: Int,
    val max: Int,
    val maxAttempts: Int? = null, // nullable: может быть "без лимита"
    val allowHints: Boolean = true
) {
    init {
        require(min < max) { "min должен быть меньше max" }
    }

    companion object {
        fun fromDifficulty(d: Difficulty): GameConfig = when (d) {
            Difficulty.EASY   -> GameConfig(0, 50,  null, allowHints = true)
            Difficulty.NORMAL -> GameConfig(0, 100, null, allowHints = true)
            Difficulty.HARD   -> GameConfig(0, 100, maxAttempts = 7, allowHints = false)
        }
    }
}

enum class Difficulty { EASY, NORMAL, HARD }