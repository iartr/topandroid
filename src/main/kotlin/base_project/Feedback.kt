package base_project

// Показ лямбды как стратегии форматирования
object Feedback {

    // Высокоуровневое форматирование отдаём лямбде: тестируется и подменяется при желании.
    private val defaultFormatter: (GuessResult, Int?) -> String = { result, remaining ->
        when (result) {
            is GuessResult.TooLow -> buildString {
                append("Моё число больше. (Δ=${result.delta})")
                if (remaining != null) append(" | Осталось попыток: $remaining")
            }
            is GuessResult.TooHigh -> buildString {
                append("Моё число меньше. (Δ=${result.delta})")
                if (remaining != null) append(" | Осталось попыток: $remaining")
            }
            is GuessResult.Correct -> "Верно! Попыток: ${result.attempts}"
            is GuessResult.OutOfRange -> "Число вне диапазона ${result.min}..${result.max}"
        }
    }

    fun format(result: GuessResult, remaining: Int?): String = defaultFormatter(result, remaining)
}
