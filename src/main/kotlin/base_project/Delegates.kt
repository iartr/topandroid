package base_project


import kotlin.reflect.KProperty

class RangeDelegate(initialValue: Int, private val range: IntRange) {
    private var value: Int = initialValue.coerceIn(range)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int = value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: Int) {
        value = newValue.coerceIn(range)
    }
}

class HistoryDelegate<T>(initialValue: T, private val maxHistory: Int = 10) {
    private val history = ArrayDeque<T>(maxHistory)
    private var current: T = initialValue

    init { history.addLast(initialValue) }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = current

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        current = newValue
        history.addLast(newValue)
        while (history.size > maxHistory) history.removeFirst()
    }

    fun getHistory(): List<T> = history.toList()
}
