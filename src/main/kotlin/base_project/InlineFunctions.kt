package base_project


inline fun measureExecutionTime(taskName: String, block: () -> Unit) {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    println("[$taskName] completed in ${end - start} ms")
}

inline fun <reified T> List<*>.sumByType(): Double {
    var sum = 0.0
    for (item in this) {
        if (item is T && item is Number) sum += item.toDouble()
    }
    return sum
}

inline fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit): T {
    if (condition) block()
    return this
}
