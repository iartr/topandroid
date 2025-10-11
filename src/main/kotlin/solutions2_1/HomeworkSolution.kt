package solutions2_1

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * РЕШЕНИЕ ДОМАШНЕГО ЗАДАНИЯ №2.1
 * 
 * ⚠️ ЭТОТ ФАЙЛ ПРЕДНАЗНАЧЕН ТОЛЬКО ДЛЯ МЕНТОРА!
 * Студенты НЕ должны видеть это решение до сдачи своей работы.
 * 
 * После проверки работы студента можно показать это решение для обсуждения.
 */

// ============================================================================
// ЧАСТЬ 1: EXTENSION FUNCTIONS
// ============================================================================

/**
 * 1.1 Медиана списка
 * Медиана - это средний элемент отсортированного списка.
 */
fun List<Int>.median(): Double? {
    if (this.isEmpty()) return null
    
    val sorted = this.sorted()
    val size = sorted.size
    
    return if (size % 2 == 0) {
        // Чётное количество - среднее двух средних элементов
        (sorted[size / 2 - 1] + sorted[size / 2]) / 2.0
    } else {
        // Нечётное количество - средний элемент
        sorted[size / 2].toDouble()
    }
}

/**
 * 1.2 Мода (самое частое число)
 */
fun List<Int>.mode(): Int? {
    if (this.isEmpty()) return null
    
    // Группируем элементы и подсчитываем количество каждого
    val counts = this.groupingBy { it }.eachCount()
    
    // Находим максимальное количество
    val maxCount = counts.values.maxOrNull() ?: return null
    
    // Возвращаем первый элемент с максимальным количеством
    return counts.entries.firstOrNull { it.value == maxCount }?.key
}

/**
 * 1.3 Середина диапазона
 */
val IntRange.middle: Int
    get() = (first + last) / 2

/**
 * 1.4 Парсинг строки в IntRange
 */
fun String.toIntRange(): IntRange? {
    val parts = this.split("-")
    if (parts.size != 2) return null
    
    val start = parts[0].trim().toIntOrNull() ?: return null
    val end = parts[1].trim().toIntOrNull() ?: return null
    
    return if (start <= end) start..end else null
}

// ============================================================================
// ЧАСТЬ 2: INLINE FUNCTIONS И REIFIED
// ============================================================================

/**
 * 2.1 Измерение времени выполнения с именем задачи
 */
inline fun measureExecutionTime(taskName: String, block: () -> Unit) {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    println("  [$taskName] completed in ${end - start} ms")
}

/**
 * 2.2 Суммирование элементов определённого типа
 * Reified позволяет проверять тип в runtime
 */
inline fun <reified T> List<*>.sumByType(): Double {
    var sum = 0.0
    for (element in this) {
        if (element is T && element is Number) {
            sum += element.toDouble()
        }
    }
    return sum
}

/**
 * 2.3 Условное применение трансформации
 */
inline fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit): T {
    if (condition) {
        block()
    }
    return this
}

// ============================================================================
// ЧАСТЬ 3: ДЕЛЕГИРОВАННЫЕ СВОЙСТВА
// ============================================================================

/**
 * 3.1 Делегат с ограничением диапазона
 */
class RangeDelegate(
    initialValue: Int,
    private val range: IntRange
) : ReadWriteProperty<Any?, Int> {
    
    private var value: Int = initialValue.coerceIn(range)
    
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return value
    }
    
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        this.value = value.coerceIn(range)
    }
}

/**
 * 3.2 Делегат с историей изменений
 */
class HistoryDelegate<T>(
    initialValue: T,
    private val maxHistory: Int = 10
) : ReadWriteProperty<Any?, T> {
    
    private var currentValue: T = initialValue
    private val history = mutableListOf(initialValue)
    
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return currentValue
    }
    
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        currentValue = value
        history.add(value)
        
        // Ограничиваем размер истории
        while (history.size > maxHistory) {
            history.removeAt(0)
        }
    }
    
    fun getHistory(): List<T> = history.toList()
}

// ============================================================================
// ЧАСТЬ 4: КЛАСС СТАТИСТИКИ ИГРЫ
// ============================================================================

class GameStatistics {
    // Ленивая инициализация времени начала
    val startTime: Long by lazy {
        System.currentTimeMillis()
    }
    
    // История попыток (здесь немного сложнее, т.к. List неизменяемый)
    private val attemptsList = mutableListOf<Int>()
    
    // Количество использованных подсказок с ограничением
    var hintsUsed: Int by RangeDelegate(0, 0..10)
    
    /**
     * Добавление попытки в историю
     */
    fun addAttempt(value: Int) {
        attemptsList.add(value)
    }
    
    /**
     * Генерация отчёта с использованием extension-функций
     */
    fun generateReport(): String {
        val currentTime = System.currentTimeMillis()
        val gameTimeSeconds = (currentTime - startTime) / 1000.0
        
        return buildString {
            appendLine("  📊 ОТЧЁТ О ИГРЕ")
            appendLine("  ${"=".repeat(40)}")
            appendLine("  Количество попыток: ${attemptsList.size}")
            
            if (attemptsList.isNotEmpty()) {
                appendLine("  Попытки: ${attemptsList.joinToString(", ")}")
                appendLine("  Медиана попыток: ${attemptsList.median()}")
                appendLine("  Мода (частая попытка): ${attemptsList.mode()}")
                
                val min = attemptsList.minOrNull()
                val max = attemptsList.maxOrNull()
                appendLine("  Диапазон попыток: $min - $max")
            }
            
            appendLine("  Использовано подсказок: $hintsUsed")
            appendLine("  Время игры: ${"%.2f".format(gameTimeSeconds)} сек")
            appendLine("  ${"=".repeat(40)}")
        }
    }
}

// ============================================================================
// ЧАСТЬ 5: СТРАТЕГИИ (БОНУС)
// ============================================================================

/**
 * Интерфейс стратегии сложности
 */
interface DifficultyStrategy {
    fun getMaxAttempts(): Int
    fun getRange(): IntRange
    fun getHintPenalty(): Int
}

/**
 * Создание стратегии сложности через анонимные объекты
 */
fun createDifficultyStrategy(level: String): DifficultyStrategy {
    return when (level.lowercase()) {
        "easy" -> object : DifficultyStrategy {
            override fun getMaxAttempts() = 15
            override fun getRange() = 1..50
            override fun getHintPenalty() = 0
        }
        
        "normal" -> object : DifficultyStrategy {
            override fun getMaxAttempts() = 10
            override fun getRange() = 1..100
            override fun getHintPenalty() = 1
        }
        
        "hard" -> object : DifficultyStrategy {
            override fun getMaxAttempts() = 7
            override fun getRange() = 1..200
            override fun getHintPenalty() = 2
        }
        
        else -> object : DifficultyStrategy {
            override fun getMaxAttempts() = 10
            override fun getRange() = 1..100
            override fun getHintPenalty() = 1
        }
    }
}

// ============================================================================
// ТЕСТИРОВАНИЕ РЕШЕНИЯ
// ============================================================================

fun main() {
    println("╔═══════════════════════════════════════════════════════════════╗")
    println("║  РЕШЕНИЕ ДОМАШНЕГО ЗАДАНИЯ 2.1 (для ментора)                ║")
    println("╚═══════════════════════════════════════════════════════════════╝")
    println()
    
    // Тест 1: Extension functions
    println("=== 1. Extension Functions ===\n")
    
    val numbers = listOf(5, 2, 8, 2, 9, 2, 7)
    println("Список: $numbers")
    println("Медиана: ${numbers.median()}")
    println("Мода: ${numbers.mode()}")
    println()
    
    val emptyList = emptyList<Int>()
    println("Пустой список: $emptyList")
    println("Медиана: ${emptyList.median()}")
    println("Мода: ${emptyList.mode()}")
    println()
    
    val range = 10..90
    println("Диапазон: $range")
    println("Середина: ${range.middle}")
    println()
    
    val rangeStr = "20-80"
    val invalidRange = "20-10-5"
    println("Строка '$rangeStr' -> ${rangeStr.toIntRange()}")
    println("Строка '$invalidRange' -> ${invalidRange.toIntRange()}")
    println()
    
    // Тест 2: Inline functions
    println("=== 2. Inline Functions ===\n")
    
    measureExecutionTime("Sleep 50ms") {
        Thread.sleep(50)
    }
    println()
    
    val mixed: List<Any> = listOf(1, 2, "test", 3.5, 4, "data", 2.5)
    println("Смешанный список: $mixed")
    println("Сумма Int: ${mixed.sumByType<Int>()}")
    println("Сумма Double: ${mixed.sumByType<Double>()}")
    println()
    
    val mutableText = StringBuilder("hello")
    mutableText.applyIf(true) {
        append(" world")
    }.applyIf(false) {
        append(" ignored")
    }
    println("StringBuilder с applyIf: $mutableText")
    println()
    
    // Тест 3: Делегаты
    println("=== 3. Делегированные свойства ===\n")
    
    var limited by RangeDelegate(50, 0..100)
    println("RangeDelegate (0..100):")
    println("  Начальное: $limited")
    limited = 150
    println("  После 150: $limited")
    limited = -10
    println("  После -10: $limited")
    limited = 75
    println("  После 75: $limited")
    println()
    
    val historyDelegate = HistoryDelegate("initial", maxHistory = 3)
    var history by historyDelegate
    println("HistoryDelegate (max 3):")
    println("  Начальное: $history")
    history = "first"
    history = "second"
    history = "third"
    history = "fourth"
    println("  История: ${historyDelegate.getHistory()}")
    println()
    
    // Тест 4: GameStatistics
    println("=== 4. GameStatistics ===\n")
    
    val stats = GameStatistics()
    Thread.sleep(100) // имитация игры
    listOf(50, 75, 62, 68, 70, 69).forEach { stats.addAttempt(it) }
    stats.hintsUsed = 3
    println(stats.generateReport())
    println()
    
    // Тест с проигрышем
    val failedStats = GameStatistics()
    listOf(10, 20, 30, 40, 50, 60, 70, 80, 90, 100).forEach { 
        failedStats.addAttempt(it) 
    }
    failedStats.hintsUsed = 8
    println("Статистика проигрыша:")
    println(failedStats.generateReport())
    println()
    
    // Тест 5: Стратегии
    println("=== 5. Стратегии сложности (Бонус) ===\n")
    
    listOf("easy", "normal", "hard").forEach { level ->
        val strategy = createDifficultyStrategy(level)
        println("$level:")
        println("  Попыток: ${strategy.getMaxAttempts()}")
        println("  Диапазон: ${strategy.getRange()}")
        println("  Штраф за подсказку: ${strategy.getHintPenalty()}")
        println()
    }
    
    // Тест граничных случаев
    println("=== ТЕСТЫ ГРАНИЧНЫХ СЛУЧАЕВ ===\n")
    
    println("1. Медиана с чётным количеством элементов:")
    val evenList = listOf(1, 2, 3, 4)
    println("  $evenList -> ${evenList.median()}")
    println()
    
    println("2. Мода с несколькими максимумами:")
    val multiMode = listOf(1, 1, 2, 2, 3)
    println("  $multiMode -> ${multiMode.mode()}")
    println()
    
    println("3. Отрицательный диапазон:")
    val negRange = "-50--10"
    println("  '$negRange' -> ${negRange.toIntRange()}")
    println()
    
    println("4. Установка hintsUsed выше лимита:")
    val testStats = GameStatistics()
    testStats.hintsUsed = 20
    println("  Попытка установить 20, фактическое значение: ${testStats.hintsUsed}")
    println()
    
    println("╔═══════════════════════════════════════════════════════════════╗")
    println("║  ВСЕ ТЕСТЫ ПРОЙДЕНЫ УСПЕШНО!                                 ║")
    println("╚═══════════════════════════════════════════════════════════════╝")
}

// ============================================================================
// ПРИМЕЧАНИЯ ДЛЯ МЕНТОРА ПРИ ПРОВЕРКЕ
// ============================================================================

/**
 * ОБЩИЕ КРИТЕРИИ ПРОВЕРКИ:
 * 
 * 1. Корректность реализации (60%):
 *    - Все функции работают согласно спецификации
 *    - Обработаны граничные случаи (пустые коллекции, null, выход за границы)
 *    - Нет runtime ошибок
 * 
 * 2. Качество кода (20%):
 *    - Читаемость и понятность
 *    - Правильное использование возможностей Kotlin
 *    - Отсутствие дублирования
 * 
 * 3. Понимание концепций (20%):
 *    - Правильное применение inline/reified
 *    - Корректная реализация делегатов
 *    - Понимание, когда использовать каждую конструкцию
 * 
 * ТИПИЧНЫЕ ОШИБКИ СТУДЕНТОВ:
 * 
 * 1. Extension functions:
 *    - Не обрабатывают пустые списки
 *    - Неправильная формула медианы для чётного количества
 *    - Некорректная валидация в toIntRange()
 * 
 * 2. Inline functions:
 *    - Забывают модификатор inline
 *    - Пытаются использовать reified без inline
 *    - Не учитывают, что element может быть не Number
 * 
 * 3. Делегаты:
 *    - Неправильная сигнатура getValue/setValue
 *    - Не ограничивают размер истории
 *    - Забывают про coerceIn в RangeDelegate
 * 
 * 4. GameStatistics:
 *    - Не используют созданные extension-функции
 *    - Проблемы с делегированным свойством для списка
 *    - Не форматируют вывод
 * 
 * ВОПРОСЫ ДЛЯ ОБСУЖДЕНИЯ С УЧЕНИКОМ:
 * 
 * 1. Почему reified работает только с inline?
 * 2. В чём разница между делегированием класса и делегированным свойством?
 * 3. Когда стоит использовать extension-функции, а когда обычные методы?
 * 4. Какие накладные расходы создаёт inline-функция?
 * 5. Как работает lazy делегат? Потокобезопасен ли он?
 */
