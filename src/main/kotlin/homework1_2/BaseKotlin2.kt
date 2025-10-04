package homework1_2

// Цель: добавить уровни сложности, лимит попыток (для HARD), список истории.
// См. "homework 1.2.md".

data class SimpleConfig(val min: Int, val max: Int, val maxAttempts: Int?)
enum class SimpleDifficulty { EASY, NORMAL, HARD }

fun main() {
    println("Игра 0..100 — уровни сложности")
    // TODO: запросить сложность через when
    // TODO: создать SimpleConfig из сложности
    // TODO: загадать число
    // TODO: вести счётчик попыток и историю (MutableList<Int>)
    // TODO: если maxAttempts исчерпан — завершить раунд
}