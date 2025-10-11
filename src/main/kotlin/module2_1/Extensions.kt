package module2_1

/**
 * ДЕМОНСТРАЦИЯ: Extension Functions (функции-расширения)
 * 
 * Extension-функции позволяют добавлять новые методы к существующим классам
 * без изменения их исходного кода и без наследования.
 * 
 * Ключевые моменты:
 * 1. Extension-функции компилируются в статические методы
 * 2. Они не имеют доступа к private-членам класса
 * 3. Разрешаются статически (важно для полиморфизма)
 * 4. При конфликте имён приоритет у методов класса
 */

// =============================================================================
// 1. ПРОСТЫЕ EXTENSION-ФУНКЦИИ
// =============================================================================

/**
 * Проверка, является ли строка палиндромом.
 * АКЦЕНТ: `this` внутри extension-функции — это receiver (объект, для которого вызывается функция)
 */
fun String.isPalindrome(): Boolean {
    val normalized = this.lowercase().filter { it.isLetterOrDigit() }
    return normalized == normalized.reversed()
}

/**
 * Получить второй элемент списка или null, если его нет.
 * АКЦЕНТ: Extension-функции могут быть дженериками
 */
fun <T> List<T>.secondOrNull(): T? {
    return if (this.size >= 2) this[1] else null
}

/**
 * Проверка, находится ли число в заданном диапазоне.
 * АКЦЕНТ: Можно создавать extension для любых типов, включая примитивы
 */
fun Int.inRange(min: Int, max: Int): Boolean {
    return this in min..max
}

// =============================================================================
// 2. EXTENSION-СВОЙСТВА
// =============================================================================

/**
 * Extension-свойство для подсчёта слов в строке.
 * АКЦЕНТ: Extension-свойства не могут иметь backing field, только getter/setter
 */
val String.wordCount: Int
    get() = this.split("\\s+".toRegex()).filter { it.isNotBlank() }.size

/**
 * Extension-свойство для получения последнего индекса списка.
 * АКЦЕНТ: Вычисляется при каждом обращении (нет кеширования)
 */
val <T> List<T>.lastIndex: Int
    get() = this.size - 1

// =============================================================================
// 3. EXTENSION ДЛЯ NULLABLE ТИПОВ
// =============================================================================

/**
 * Возвращает строку или значение по умолчанию, если строка null.
 * АКЦЕНТ: Extension-функции работают и с nullable типами (String?)
 */
fun String?.orDefault(default: String = "N/A"): String {
    return this ?: default
}

/**
 * Безопасное преобразование строки в Int с дефолтным значением.
 * АКЦЕНТ: Удобно для обработки пользовательского ввода
 */
fun String?.toIntOrDefault(default: Int = 0): Int {
    return this?.toIntOrNull() ?: default
}

// =============================================================================
// 4. ПРАКТИЧЕСКИЕ EXTENSION ДЛЯ ИГРЫ
// =============================================================================

/**
 * Случайное число из диапазона, исключая заданные значения.
 * АКЦЕНТ: Extension-функции делают API более выразительным и читаемым
 */
fun IntRange.randomExcluding(exclude: Set<Int>): Int? {
    val available = this.filter { it !in exclude }
    return available.randomOrNull()
}

/**
 * Получить последние N элементов списка (безопасно).
 * АКЦЕНТ: Можно создавать утилиты, которые выглядят как встроенные методы
 */
fun <T> List<T>.lastN(n: Int): List<T> {
    return this.takeLast(n.coerceAtMost(this.size))
}

/**
 * Форматирование списка чисел для вывода статистики.
 * АКЦЕНТ: Extension-функции помогают инкапсулировать логику форматирования
 */
fun List<Int>.formatAsAttempts(): String {
    if (this.isEmpty()) return "Нет попыток"
    return "Попытки: ${this.joinToString(", ")} (всего: ${this.size})"
}

/**
 * Вычисление среднего значения для списка чисел.
 * АКЦЕНТ: Extension может возвращать nullable тип для обработки пустых коллекций
 */
fun List<Int>.averageOrNull(): Double? {
    return if (this.isEmpty()) null else this.average()
}

// =============================================================================
// 5. EXTENSION С ФУНКЦИЯМИ ВЫСШЕГО ПОРЯДКА
// =============================================================================

/**
 * Применить преобразование к строке, если она не пустая.
 * АКЦЕНТ: Extension-функции могут принимать лямбды, создавая DSL-подобный синтаксис
 */
inline fun String.ifNotEmpty(transform: (String) -> String): String {
    return if (this.isNotEmpty()) transform(this) else this
}

/**
 * Выполнить действие для каждого элемента с индексом (более читаемая версия).
 * АКЦЕНТ: Можно создавать "сахарные" обёртки над стандартными функциями
 */
inline fun <T> List<T>.forEachIndexed(action: (index: Int, item: T) -> Unit) {
    for ((index, item) in this.withIndex()) {
        action(index, item)
    }
}

// =============================================================================
// ДЕМОНСТРАЦИОННАЯ ФУНКЦИЯ
// =============================================================================

/**
 * Функция для демонстрации всех extension-функций.
 */
fun demonstrateExtensions() {
    println("=== ДЕМОНСТРАЦИЯ EXTENSION FUNCTIONS ===\n")
    
    // 1. Палиндром
    println("1. Проверка палиндрома:")
    println("  'А роза упала на лапу Азора'.isPalindrome() = ${"А роза упала на лапу Азора".isPalindrome()}")
    println("  'Hello'.isPalindrome() = ${"Hello".isPalindrome()}\n")
    
    // 2. Второй элемент списка
    println("2. Получение второго элемента:")
    val numbers = listOf(10, 20, 30)
    println("  $numbers.secondOrNull() = ${numbers.secondOrNull()}")
    println("  ${listOf(1)}.secondOrNull() = ${listOf(1).secondOrNull()}\n")
    
    // 3. Диапазон
    println("3. Проверка диапазона:")
    println("  50.inRange(0, 100) = ${50.inRange(0, 100)}")
    println("  150.inRange(0, 100) = ${150.inRange(0, 100)}\n")
    
    // 4. Extension-свойства
    println("4. Extension-свойства:")
    val text = "Hello world from Kotlin"
    println("  '$text'.wordCount = ${text.wordCount}")
    println("  $numbers.lastIndex = ${numbers.lastIndex}\n")
    
    // 5. Nullable extensions
    println("5. Работа с nullable:")
    val nullStr: String? = null
    println("  null.orDefault() = ${nullStr.orDefault()}")
    println("  null.toIntOrDefault(42) = ${nullStr.toIntOrDefault(42)}\n")
    
    // 6. Практические примеры
    println("6. Практические примеры для игры:")
    val range = 1..10
    val exclude = setOf(3, 5, 7)
    println("  $range.randomExcluding($exclude) = ${range.randomExcluding(exclude)}")
    
    val attempts = listOf(50, 75, 62, 68, 70)
    println("  $attempts.lastN(3) = ${attempts.lastN(3)}")
    println("  ${attempts.formatAsAttempts()}")
    println("  Среднее: ${attempts.averageOrNull()}\n")
    
    // 7. Extension с лямбдами
    println("7. Extension с лямбдами:")
    val input = "  hello  "
    val processed = input.trim().ifNotEmpty { it.uppercase() }
    println("  '$input' -> '$processed'\n")
    
    // 8. ForEach с индексом
    println("8. Итерация с индексом:")
    listOf("первый", "второй", "третий").forEachIndexed { index, item ->
        println("  [$index] = $item")
    }
}
