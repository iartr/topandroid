package homework1_3

sealed class GameResult {
    data class Up(val delta: Int): GameResult()
    data class Down(val delta: Int): GameResult()
    data class Win(val attempts: Int): GameResult()
}

class History<T> : Iterable<T> {
    private val items = mutableListOf<T>()
    override fun iterator(): Iterator<T> = items.iterator()
    fun add(item: T) { items += item }
    fun copy(): List<T> = items.toList()
}

fun main() {
    val secret = (0..100).random()
    var attempts = 0
    val hist = History<Int>()

    val formatter: (GameResult) -> String = { result ->
        when (result) {
            is GameResult.Up   -> "Моё число больше (Δ=${result.delta})"
            is GameResult.Down -> "Моё число меньше (Δ=${result.delta})"
            is GameResult.Win  -> "Угадано за ${result.attempts}"
        }
    }

    while (true) {
        val guess = readlnOrNull()?.toIntOrNull() ?: continue
        attempts++
        hist.add(guess)
        val res: GameResult = when {
            guess < secret -> GameResult.Up(secret - guess)
            guess > secret -> GameResult.Down(guess - secret)
            else -> GameResult.Win(attempts)
        }
        println(formatter(res))
        if (res is GameResult.Win) break
    }

    println("История: ${hist.copy().joinToString()}")
}