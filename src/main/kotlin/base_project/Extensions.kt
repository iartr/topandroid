package base_project

fun List<Int>.median(): Double? {
    if (this.isEmpty()) return null
    val sorted = this.sorted()
    val n = sorted.size
    return if (n % 2 == 1) sorted[n / 2].toDouble()
    else (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0
}

fun List<Int>.mode(): Int? {
    if (this.isEmpty()) return null
    val freq = this.groupingBy { it }.eachCount()
    return freq.maxByOrNull { it.value }?.key
}

val IntRange.middle: Int
    get() = (first + last) / 2

fun String.toIntRange(): IntRange? {
    val regex = """^\s*(\d+)\s*-\s*(\d+)\s*$""".toRegex()
    val m = regex.matchEntire(this) ?: return null
    val a = m.groupValues[1].toInt()
    val b = m.groupValues[2].toInt()
    return if (a <= b) a..b else b..a
}