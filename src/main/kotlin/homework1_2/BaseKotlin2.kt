package homework1_2

data class SimpleConfig(val min: Int, val max: Int, val maxAttempts: Int?)
enum class SimpleDifficulty { EASY, NORMAL, HARD }

fun main() {
    val difficulty = askDiff()
    val cfg = when (difficulty) {
        SimpleDifficulty.EASY   -> SimpleConfig(0, 50, null)
        SimpleDifficulty.NORMAL -> SimpleConfig(0, 100, null)
        SimpleDifficulty.HARD   -> SimpleConfig(0, 100, 7)
    }
    val secret = (cfg.min..cfg.max).random()
    val history = mutableListOf<Int>()
    var attempts = 0

    while (true) {
        if (cfg.maxAttempts != null && attempts >= cfg.maxAttempts) {
            println("Поражение. Попытки закончились. Было: ${history.joinToString()}")
            return
        }
        print("Введите число: ")
        val guess = readlnOrNull()?.toIntOrNull() ?: continue
        history += guess
        attempts++
        when {
            guess < secret -> println("Больше")
            guess > secret -> println("Меньше")
            else -> {
                println("Победа! История: ${history.joinToString()}")
                return
            }
        }
    }
}

private fun askDiff(): SimpleDifficulty {
    while (true) {
        println("1)EASY 2)NORMAL 3)HARD")
        when (readlnOrNull()?.trim()) {
            "1" -> return SimpleDifficulty.EASY
            "2" -> return SimpleDifficulty.NORMAL
            "3" -> return SimpleDifficulty.HARD
        }
    }
}