package homework2_1

/**
 * ДОМАШНЕЕ ЗАДАНИЕ №2.1: Продвинутый Kotlin
 * 
 * В этом задании вы примените полученные знания о:
 * - Extension functions
 * - Inline functions с reified
 * - Делегированных свойствах
 * - Анонимных объектах
 * 
 * ЗАДАЧА: Создать систему анализа статистики игры "Угадай число"
 * 
 * ============================================================================
 * ЧАСТЬ 1: EXTENSION FUNCTIONS (20 баллов)
 * ============================================================================
 * 
 * Создайте следующие extension-функции:
 */

// TODO 1.1 (5 баллов): Создайте extension-функцию для List<Int>
// fun List<Int>.median(): Double?
// Которая возвращает медиану списка (среднее значение в отсортированном списке)
// Если список пустой - возвращает null
// Если количество элементов чётное - среднее арифметическое двух средних элементов

// TODO 1.2 (5 баллов): Создайте extension-функцию для List<Int>
// fun List<Int>.mode(): Int?
// Которая возвращает моду (самое частое число) или null, если список пуст
// Если несколько чисел встречаются одинаково часто, верните любое из них

// TODO 1.3 (5 баллов): Создайте extension-свойство для IntRange
// val IntRange.middle: Int
// Которое возвращает среднее значение диапазона

// TODO 1.4 (5 баллов): Создайте extension-функцию для String
// fun String.toIntRange(): IntRange?
// Которая парсит строку вида "10-50" в IntRange(10, 50)
// Возвращает null, если формат неверный

/**
 * ============================================================================
 * ЧАСТЬ 2: INLINE FUNCTIONS И REIFIED (25 баллов)
 * ============================================================================
 */

// TODO 2.1 (10 баллов): Создайте inline-функцию
// inline fun measureExecutionTime(taskName: String, block: () -> Unit)
// Которая выводит название задачи, выполняет block и выводит время выполнения в мс
// Формат вывода: "[taskName] completed in X ms"

// TODO 2.2 (10 баллов): Создайте inline-функцию с reified
// inline fun <reified T> List<*>.sumByType(): Double
// Которая суммирует все элементы типа T, если они Number
// Для других типов игнорирует элементы
// Пример: listOf(1, "test", 2.5, 3).sumByType<Int>() вернёт 4.0

// TODO 2.3 (5 баллов): Создайте inline-функцию
// inline fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit): T
// Которая применяет block к объекту только если condition == true
// Возвращает сам объект (для chaining)

/**
 * ============================================================================
 * ЧАСТЬ 3: ДЕЛЕГИРОВАННЫЕ СВОЙСТВА (30 баллов)
 * ============================================================================
 */

// TODO 3.1 (15 баллов): Создайте делегат RangeDelegate
// class RangeDelegate(initialValue: Int, private val range: IntRange)
// Который автоматически ограничивает значение в заданном диапазоне
// При попытке установить значение вне диапазона, устанавливается ближайшее допустимое

// TODO 3.2 (15 баллов): Создайте делегат HistoryDelegate<T>
// class HistoryDelegate<T>(initialValue: T, private val maxHistory: Int = 10)
// Который хранит историю последних N изменений значения
// Добавьте метод: fun getHistory(): List<T>

/**
 * ============================================================================
 * ЧАСТЬ 4: ИНТЕГРАЦИЯ - КЛАСС СТАТИСТИКИ ИГРЫ (25 баллов)
 * ============================================================================
 */

// TODO 4.1 (25 баллов): Создайте класс GameStatistics
class GameStatistics {
    // TODO: Используйте делегированное свойство lazy для хранения времени начала
    // val startTime: Long by lazy { ... }
    
    // TODO: Используйте HistoryDelegate для хранения истории попыток
    // var attempts: List<Int> by HistoryDelegate(emptyList())
    
    // TODO: Используйте RangeDelegate для хранения количества подсказок (0..10)
    // var hintsUsed: Int by RangeDelegate(0, 0..10)
    
    // TODO: Реализуйте метод addAttempt(value: Int)
    // Который добавляет попытку в историю
    
    // TODO: Реализуйте метод generateReport(): String
    // Который формирует отчёт с использованием ваших extension-функций:
    // - Количество попыток
    // - Медиана попыток (используйте median())
    // - Мода попыток (используйте mode())
    // - Время игры в секундах
    // - Использовано подсказок
}

/**
 * ============================================================================
 * ЧАСТЬ 5: АНОНИМНЫЕ ОБЪЕКТЫ И СТРАТЕГИИ (Бонус +10 баллов)
 * ============================================================================
 */

// TODO 5.1 (10 баллов): Создайте интерфейс DifficultyStrategy
interface DifficultyStrategy {
    fun getMaxAttempts(): Int
    fun getRange(): IntRange
    fun getHintPenalty(): Int // штраф за использование подсказки
}

// TODO 5.2: Создайте функцию createDifficultyStrategy
// fun createDifficultyStrategy(level: String): DifficultyStrategy
// Которая возвращает анонимный объект DifficultyStrategy в зависимости от уровня:
// - "easy": 15 попыток, диапазон 1..50, штраф 0
// - "normal": 10 попыток, диапазон 1..100, штраф 1
// - "hard": 7 попыток, диапазон 1..200, штраф 2

/**
 * ============================================================================
 * КРИТЕРИИ ОЦЕНКИ
 * ============================================================================
 * 
 * - Extension functions работают корректно: 20 баллов
 * - Inline functions с reified реализованы: 25 баллов
 * - Делегаты работают правильно: 30 баллов
 * - Класс GameStatistics полностью функционален: 25 баллов
 * - Бонус за стратегии: 10 баллов
 * 
 * ИТОГО: 100 баллов (110 с бонусом)
 * 
 * ============================================================================
 * ПОДСКАЗКИ
 * ============================================================================
 * 
 * 1. Для медианы отсортируйте список и возьмите средний элемент
 * 2. Для моды используйте groupingBy { it }.eachCount()
 * 3. Для reified используйте is для проверки типа
 * 4. Делегаты должны реализовывать ReadWriteProperty<Any?, T>
 * 5. Не забывайте про операторы getValue и setValue
 * 
 * ============================================================================
 * ТЕСТИРОВАНИЕ
 * ============================================================================
 * 
 * Раскомментируйте функцию main ниже после выполнения всех TODO
 */

/*
fun main() {
    println("=== ТЕСТИРОВАНИЕ ДОМАШНЕГО ЗАДАНИЯ 2.1 ===\n")
    
    // Тест 1: Extension functions
    println("1. Тест extension functions:")
    val numbers = listOf(5, 2, 8, 2, 9, 2, 7)
    println("  Список: $numbers")
    println("  Медиана: ${numbers.median()}")
    println("  Мода: ${numbers.mode()}")
    
    val range = 10..90
    println("  Диапазон: $range")
    println("  Середина: ${range.middle}")
    
    val rangeStr = "20-80"
    println("  Строка: '$rangeStr'")
    println("  Как диапазон: ${rangeStr.toIntRange()}")
    println()
    
    // Тест 2: Inline functions
    println("2. Тест inline functions:")
    measureExecutionTime("Sleep test") {
        Thread.sleep(50)
    }
    
    val mixed: List<Any> = listOf(1, 2, "test", 3.5, 4, "data", 2.5)
    println("  Смешанный список: $mixed")
    println("  Сумма Int: ${mixed.sumByType<Int>()}")
    println("  Сумма Double: ${mixed.sumByType<Double>()}")
    
    val str = "hello".applyIf(true) {
        uppercase()
    }
    println("  'hello'.applyIf(true) { uppercase() } = $str")
    println()
    
    // Тест 3: Делегированные свойства
    println("3. Тест делегированных свойств:")
    var limited by RangeDelegate(50, 0..100)
    println("  Начальное значение: $limited")
    limited = 150
    println("  После установки 150: $limited")
    limited = -10
    println("  После установки -10: $limited")
    
    var history by HistoryDelegate("initial", maxHistory = 3)
    history = "first"
    history = "second"
    history = "third"
    history = "fourth"
    println("  История (макс 3): ${(history as? HistoryDelegate<String>)?.getHistory()}")
    println()
    
    // Тест 4: GameStatistics
    println("4. Тест GameStatistics:")
    val stats = GameStatistics()
    listOf(50, 75, 62, 68, 70).forEach { stats.addAttempt(it) }
    stats.hintsUsed = 3
    println(stats.generateReport())
    println()
    
    // Тест 5: Стратегии (бонус)
    println("5. Тест стратегий:")
    val easyStrategy = createDifficultyStrategy("easy")
    println("  Easy: ${easyStrategy.getMaxAttempts()} попыток, ${easyStrategy.getRange()}, штраф ${easyStrategy.getHintPenalty()}")
    
    val hardStrategy = createDifficultyStrategy("hard")
    println("  Hard: ${hardStrategy.getMaxAttempts()} попыток, ${hardStrategy.getRange()}, штраф ${hardStrategy.getHintPenalty()}")
    
    println("\n=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===")
}
*/

/**
 * ============================================================================
 * ВАЖНЫЕ ЗАМЕЧАНИЯ ДЛЯ МЕНТОРА
 * ============================================================================
 * 
 * При проверке обратите внимание на:
 * 
 * 1. Extension functions:
 *    - Корректная обработка пустых списков
 *    - Правильная работа с чётным/нечётным количеством элементов в медиане
 *    - Валидация формата строки в toIntRange()
 * 
 * 2. Inline functions:
 *    - Модификатор inline присутствует
 *    - reified используется правильно
 *    - Проверка типов через is
 * 
 * 3. Делегаты:
 *    - Правильная реализация ReadWriteProperty
 *    - Корректная работа с границами в RangeDelegate
 *    - Ограничение размера истории в HistoryDelegate
 * 
 * 4. Интеграция:
 *    - Все делегированные свойства работают
 *    - Отчёт содержит всю необходимую информацию
 *    - Используются созданные extension-функции
 * 
 * 5. Код-стайл:
 *    - Читаемый код с понятными названиями
 *    - Комментарии к сложным местам
 *    - Обработка граничных случаев
 */
