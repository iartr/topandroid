package base_project

// sealed class — ограниченная иерархия (наследование внутри одного файла/модуля).
// Удобно для "результатов" и исчерпывающих when.
sealed class GuessResult {
    data class TooLow(val delta: Int) : GuessResult()
    data class TooHigh(val delta: Int) : GuessResult()
    data class Correct(val attempts: Int) : GuessResult()

    data class OutOfRange(val min: Int, val max: Int) : GuessResult()
}
