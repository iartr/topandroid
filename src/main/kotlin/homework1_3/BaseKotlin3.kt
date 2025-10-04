package homework1_3

// Цель: вынести форматирование ответа в лямбду + дженерик-коллекцию History<T>.
// См. "homework 1.3.md".

sealed class MiniResult {
    data class Up(val delta: Int): MiniResult()
    data class Down(val delta: Int): MiniResult()
    data class Win(val attempts: Int): MiniResult()
}

class History<T> : Iterable<T> {
    private val items = mutableListOf<T>()
    override fun iterator(): Iterator<T> = items.iterator()

    fun add(item: T) {
        items.add(item)
    }

    fun toList(): List<T> = items.toList()
}

fun main() {
    // TODO: собрать простую логику игры
    // TODO: выделить (MiniResult) и лямбду (MiniResult) -> String
    // TODO: сохранять attempts в History<Int>, по завершении вывести сводку
}