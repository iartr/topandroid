package module2_1

/**
 * ДЕМОНСТРАЦИЯ: Inline Functions и модификаторы
 * 
 * Inline-функции встраиваются компилятором в место вызова, устраняя накладные
 * расходы на создание объектов Function и вызов методов.
 * 
 * Ключевые моменты:
 * 1. inline устраняет создание объектов для лямбд
 * 2. reified позволяет работать с типами в runtime (только с inline!)
 * 3. noinline отключает inlining для конкретного параметра-лямбды
 * 4. crossinline запрещает non-local returns в лямбде
 * 5. ПОСМОТРЕТЬ В БАЙТКОДЕ: Tools → Kotlin → Show Kotlin Bytecode → Decompile
 */

// =============================================================================
// 1. БАЗОВАЯ INLINE-ФУНКЦИЯ
// =============================================================================

/**
 * Измерение времени выполнения блока кода.
 * АКЦЕНТ: Без inline создавался бы объект Function0, с inline — код встраивается.
 * ПОКАЗАТЬ: декомпилируйте в Java и покажите, что код block() встроен прямо в вызывающую функцию.
 */
inline fun measureTimeMillis(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    return end - start
}

/**
 * Повторить действие N раз.
 * АКЦЕНТ: inline позволяет использовать non-local return в лямбде.
 */
inline fun repeat(times: Int, action: (Int) -> Unit) {
    for (i in 0 until times) {
        action(i)
    }
}

/**
 * Выполнить блок кода и вернуть результат вместе со временем выполнения.
 * АКЦЕНТ: inline-функции могут возвращать результат лямбды.
 */
inline fun <T> measureTime(block: () -> T): Pair<T, Long> {
    val start = System.currentTimeMillis()
    val result = block()
    val time = System.currentTimeMillis() - start
    return result to time
}

// =============================================================================
// 2. REIFIED TYPE PARAMETERS
// =============================================================================

/**
 * Проверка типа объекта с использованием reified.
 * АКЦЕНТ: Без reified это невозможно из-за type erasure.
 * ПОКАЗАТЬ: попробуйте убрать inline и reified — будет ошибка компиляции.
 */
inline fun <reified T> isInstance(value: Any): Boolean {
    return value is T
}

/**
 * Фильтрация списка по типу элементов.
 * АКЦЕНТ: Стандартная библиотека использует именно такую реализацию.
 */
inline fun <reified T> List<*>.filterByType(): List<T> {
    return this.filter { it is T }.map { it as T }
}

/**
 * Безопасное приведение типа.
 * АКЦЕНТ: Reified позволяет создавать type-safe утилиты.
 */
inline fun <reified T> Any?.safeCast(): T? {
    return this as? T
}

/**
 * Получить имя класса типа.
 * АКЦЕНТ: Доступ к KClass<T> возможен только благодаря reified.
 */
inline fun <reified T> getTypeName(): String {
    return T::class.simpleName ?: "Unknown"
}

/**
 * Подсчёт элементов определённого типа в списке.
 * АКЦЕНТ: Практический пример для обработки гетерогенных коллекций.
 */
inline fun <reified T> List<*>.countOfType(): Int {
    return this.count { it is T }
}

// =============================================================================
// 3. NOINLINE MODIFIER
// =============================================================================

/**
 * Функция с inline и noinline параметрами.
 * АКЦЕНТ: noinline нужен, когда лямбду нужно сохранить или передать дальше.
 * 
 * inlineAction встраивается в место вызова.
 * callback НЕ встраивается, т.к. мы его сохраняем и возвращаем.
 */
inline fun processWithCallback(
    inlineAction: () -> Unit,
    noinline callback: () -> Unit
): () -> Unit {
    inlineAction() // встраивается
    
    // callback нельзя встроить, т.к. мы его сохраняем
    val savedCallback = callback
    return savedCallback
}

/**
 * Обработка данных с логированием.
 * АКЦЕНТ: logger помечен noinline, чтобы можно было передать его в другую функцию.
 */
inline fun <T> processData(
    data: T,
    processor: (T) -> T,
    noinline logger: (String) -> Unit
): T {
    logger("Начало обработки")
    val result = processor(data)
    logger("Обработка завершена")
    
    // Передаём logger в другую функцию (не inline)
    saveLog(logger)
    
    return result
}

// Вспомогательная функция для демонстрации noinline
fun saveLog(logger: (String) -> Unit) {
    logger("Лог сохранён")
}

// =============================================================================
// 4. CROSSINLINE MODIFIER
// =============================================================================

/**
 * Асинхронное выполнение с crossinline.
 * АКЦЕНТ: crossinline запрещает non-local return, т.к. action выполняется в другом контексте.
 * 
 * Без crossinline компилятор не позволил бы использовать action внутри Thread,
 * т.к. non-local return из другого потока некорректен.
 */
inline fun executeAsync(crossinline action: () -> Unit) {
    Thread {
        println("[Thread ${Thread.currentThread().name}] Выполнение начато")
        action() // action не может содержать return, который выйдет из executeAsync
        println("[Thread ${Thread.currentThread().name}] Выполнение завершено")
    }.start()
}

/**
 * Обработка списка с асинхронными операциями.
 * АКЦЕНТ: crossinline нужен, когда лямбда используется внутри другой лямбды или класса.
 */
inline fun <T> List<T>.forEachAsync(crossinline action: (T) -> Unit) {
    for (item in this) {
        Thread {
            action(item)
        }.start()
    }
}

/**
 * Выполнение с повторными попытками.
 * АКЦЕНТ: crossinline позволяет вызывать action внутри вложенной лямбды.
 */
inline fun <T> retryOnFailure(
    maxAttempts: Int = 3,
    crossinline action: () -> T
): T? {
    repeat(maxAttempts) { attempt ->
        try {
            return action() // local return из retryOnFailure
        } catch (e: Exception) {
            if (attempt == maxAttempts - 1) {
                println("Все попытки исчерпаны")
                return null
            }
            println("Попытка ${attempt + 1} не удалась, повтор...")
        }
    }
    return null
}

// =============================================================================
// 5. КОМБИНАЦИЯ МОДИФИКАТОРОВ
// =============================================================================

/**
 * Сложный пример с несколькими модификаторами.
 * АКЦЕНТ: Можно комбинировать inline, reified, crossinline, noinline.
 * 
 * @param transform - inline, встраивается
 * @param onSuccess - crossinline, вызывается в другом контексте
 * @param onError - noinline, сохраняется для последующего использования
 */
inline fun <reified T, R> transformSafely(
    value: Any?,
    transform: (T) -> R,
    crossinline onSuccess: (R) -> Unit,
    noinline onError: (String) -> Unit
): R? {
    return when {
        value == null -> {
            onError("Значение null")
            null
        }
        value !is T -> {
            onError("Тип ${value::class.simpleName} не соответствует ${T::class.simpleName}")
            null
        }
        else -> {
            try {
                val result = transform(value)
                Thread {
                    onSuccess(result) // crossinline позволяет использовать в Thread
                }.start()
                result
            } catch (e: Exception) {
                saveErrorLog(onError, e.message ?: "Unknown error")
                null
            }
        }
    }
}

fun saveErrorLog(logger: (String) -> Unit, message: String) {
    logger("Ошибка: $message")
}

// =============================================================================
// 6. NON-LOCAL RETURNS
// =============================================================================

/**
 * Поиск первого чётного числа.
 * АКЦЕНТ: Non-local return позволяет выйти из findFirstEven прямо из лямбды.
 */
inline fun List<Int>.findFirstEven(): Int? {
    forEach { num ->
        if (num % 2 == 0) {
            return num // выход из findFirstEven, не только из forEach
        }
    }
    return null
}

/**
 * Проверка валидности с ранним выходом.
 * АКЦЕНТ: Без inline такой return был бы невозможен.
 */
inline fun validateInput(input: String, validator: (String) -> Boolean): Boolean {
    if (input.isBlank()) {
        return false // early return
    }
    return validator(input)
}

// =============================================================================
// ДЕМОНСТРАЦИОННАЯ ФУНКЦИЯ
// =============================================================================

fun demonstrateInlineFunctions() {
    println("=== ДЕМОНСТРАЦИЯ INLINE FUNCTIONS ===\n")
    
    // 1. Измерение времени
    println("1. Измерение времени выполнения:")
    val time = measureTimeMillis {
        Thread.sleep(100)
        println("  Выполняется блок кода...")
    }
    println("  Время: $time мс\n")
    
    // 2. Измерение с результатом
    println("2. Измерение с возвратом результата:")
    val (result, executionTime) = measureTime {
        (1..1000).sum()
    }
    println("  Результат: $result, Время: $executionTime мс\n")
    
    // 3. Reified - проверка типа
    println("3. Reified - проверка типа:")
    println("  isInstance<String>(\"Hello\") = ${isInstance<String>("Hello")}")
    println("  isInstance<Int>(\"Hello\") = ${isInstance<Int>("Hello")}\n")
    
    // 4. Фильтрация по типу
    println("4. Фильтрация по типу:")
    val mixed: List<Any> = listOf(1, "два", 3, "четыре", 5.0, "шесть")
    val strings: List<String> = mixed.filterByType()
    println("  Исходный список: $mixed")
    println("  Только строки: $strings")
    println("  Количество чисел: ${mixed.countOfType<Int>()}\n")
    
    // 5. Safe cast
    println("5. Безопасное приведение типа:")
    val value: Any = "42"
    val asString: String? = value.safeCast()
    val asInt: Int? = value.safeCast()
    println("  Значение: $value")
    println("  Как String: $asString")
    println("  Как Int: $asInt\n")
    
    // 6. Имя типа
    println("6. Получение имени типа:")
    println("  getTypeName<String>() = ${getTypeName<String>()}")
    println("  getTypeName<List<Int>>() = ${getTypeName<List<Int>>()}\n")
    
    // 7. Noinline
    println("7. Noinline - сохранение callback:")
    val savedCallback = processWithCallback(
        inlineAction = { println("  Inline action выполнено") },
        callback = { println("  Callback вызван") }
    )
    println("  Вызов сохранённого callback:")
    savedCallback()
    println()
    
    // 8. Crossinline (асинхронность)
    println("8. Crossinline - асинхронное выполнение:")
    executeAsync {
        Thread.sleep(50)
        println("  Асинхронная задача выполнена")
    }
    Thread.sleep(100) // ждём завершения async задачи
    println()
    
    // 9. Non-local return
    println("9. Non-local return:")
    val numbers = listOf(1, 3, 5, 8, 9)
    val firstEven = numbers.findFirstEven()
    println("  Список: $numbers")
    println("  Первое чётное: $firstEven\n")
    
    // 10. Комбинация модификаторов
    println("10. Комбинация модификаторов:")
    transformSafely<String, Int>(
        value = "42",
        transform = { it.toInt() },
        onSuccess = { println("  Успешно преобразовано: $it") },
        onError = { println("  Ошибка: $it") }
    )
    Thread.sleep(50) // ждём onSuccess
}

// =============================================================================
// СРАВНЕНИЕ: INLINE vs БЕЗ INLINE (для байткода)
// =============================================================================

/**
 * Функция БЕЗ inline (для сравнения в байткоде).
 * МЕНТОРУ: Показать разницу в декомпилированном Java-коде.
 */
fun measureTimeNoInline(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    return end - start
}

/**
 * Функция С inline (для сравнения в байткоде).
 * МЕНТОРУ: Декомпилировать обе функции и показать, что здесь нет создания объекта Function0.
 */
inline fun measureTimeWithInline(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    return end - start
}
