package module2_1

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * ДЕМОНСТРАЦИЯ: Делегирование (Delegation)
 * 
 * Kotlin поддерживает делегирование на уровне языка:
 * 1. Делегирование класса (by для интерфейсов)
 * 2. Делегированные свойства (by для свойств)
 * 3. Анонимные объекты (object expressions)
 * 
 * Ключевые моменты:
 * 1. Делегирование класса устраняет boilerplate код
 * 2. Встроенные делегаты: lazy, observable, vetoable, map
 * 3. Можно создавать собственные делегаты
 * 4. Анонимные объекты для реализации интерфейсов на лету
 * 5. ПОКАЗАТЬ В БАЙТКОДЕ: как компилируется делегирование
 */

// =============================================================================
// 1. ДЕЛЕГИРОВАНИЕ КЛАССА (Class Delegation)
// =============================================================================

/**
 * Базовый интерфейс для источника данных.
 */
interface DataSource {
    fun fetchData(): String
    fun isAvailable(): Boolean
}

/**
 * Реализация удалённого источника данных.
 */
class RemoteDataSource : DataSource {
    override fun fetchData(): String {
        println("  [RemoteDataSource] Загрузка данных с сервера...")
        return "Данные с сервера"
    }
    
    override fun isAvailable(): Boolean {
        println("  [RemoteDataSource] Проверка доступности...")
        return true
    }
}

/**
 * Репозиторий с делегированием.
 * АКЦЕНТ: Ключевое слово `by` автоматически делегирует все методы интерфейса.
 * ПОКАЗАТЬ В БАЙТКОДЕ: компилятор создаёт приватное поле и перенаправляет вызовы.
 */
class Repository(private val dataSource: DataSource) : DataSource by dataSource {
    // Весь интерфейс DataSource делегирован в dataSource
    // Компилятор автоматически генерирует:
    // override fun fetchData() = dataSource.fetchData()
    // override fun isAvailable() = dataSource.isAvailable()
    
    // Но мы можем переопределить любой метод:
    override fun fetchData(): String {
        println("  [Repository] Дополнительная обработка...")
        return dataSource.fetchData().uppercase()
    }
}

/**
 * Пример с кешированием через делегирование.
 */
interface Logger {
    fun log(message: String)
    fun error(message: String)
}

class ConsoleLogger : Logger {
    override fun log(message: String) {
        println("  [LOG] $message")
    }
    
    override fun error(message: String) {
        println("  [ERROR] $message")
    }
}

/**
 * Логгер с кешированием сообщений.
 * АКЦЕНТ: Делегируем базовую функциональность, добавляем кеширование.
 */
class CachedLogger(logger: Logger) : Logger by logger {
    private val cache = mutableListOf<String>()
    
    override fun log(message: String) {
        cache.add("[${System.currentTimeMillis()}] $message")
        println("  [CACHED LOG] $message")
    }
    
    fun showCache() {
        println("  Кеш логов (${cache.size} записей):")
        cache.forEach { println("    $it") }
    }
}

// =============================================================================
// 2. ВСТРОЕННЫЕ ДЕЛЕГИРОВАННЫЕ СВОЙСТВА
// =============================================================================

/**
 * Демонстрация встроенных делегатов свойств.
 */
class PropertyDelegatesDemo {
    
    // 2.1 lazy - ленивая инициализация
    /**
     * АКЦЕНТ: Значение вычисляется только при первом обращении.
     * По умолчанию потокобезопасен (synchronized).
     */
    val expensiveValue: String by lazy {
        println("    [lazy] Вычисление expensiveValue...")
        Thread.sleep(100)
        "Дорогостоящее значение"
    }
    
    // 2.2 observable - отслеживание изменений
    /**
     * АКЦЕНТ: Callback вызывается после каждого изменения значения.
     */
    var name: String by Delegates.observable("Unknown") { property, oldValue, newValue ->
        println("    [observable] ${property.name}: $oldValue -> $newValue")
    }
    
    // 2.3 vetoable - проверка перед изменением
    /**
     * АКЦЕНТ: Изменение применяется только если лямбда вернула true.
     */
    var age: Int by Delegates.vetoable(0) { property, oldValue, newValue ->
        val isValid = newValue >= 0 && newValue <= 150
        if (!isValid) {
            println("    [vetoable] Отклонено: возраст $newValue недопустим")
        }
        isValid
    }
    
    // 2.4 notNull - инициализация позже (но не null)
    /**
     * АКЦЕНТ: Бросает исключение при чтении, если значение не установлено.
     * Альтернатива lateinit для примитивов.
     */
    var score: Int by Delegates.notNull()
}

/**
 * Делегирование в Map (для десериализации).
 * АКЦЕНТ: Удобно для работы с JSON, конфигурационными файлами.
 */
class UserFromMap(map: Map<String, Any?>) {
    val name: String by map
    val age: Int by map
    val email: String by map
    
    override fun toString() = "User(name=$name, age=$age, email=$email)"
}

// =============================================================================
// 3. СОБСТВЕННЫЕ ДЕЛЕГАТЫ
// =============================================================================

/**
 * Делегат с логированием всех операций.
 * АКЦЕНТ: Для создания делегата нужны операторы getValue и setValue.
 */
class LoggingDelegate<T>(private var value: T) : ReadWriteProperty<Any?, T> {
    
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        println("    [Delegate] Чтение ${property.name} = $value")
        return value
    }
    
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        println("    [Delegate] Запись ${property.name}: ${this.value} -> $value")
        this.value = value
    }
}

/**
 * Делегат с ограничением значения.
 * АКЦЕНТ: Автоматически обрезает значение до допустимого диапазона.
 */
class LimitedDelegate(
    private var value: Int,
    private val min: Int,
    private val max: Int
) : ReadWriteProperty<Any?, Int> {
    
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return value
    }
    
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        this.value = value.coerceIn(min, max)
        if (value < min || value > max) {
            println("    [Limited] ${property.name}: $value обрезано до ${this.value}")
        }
    }
}

/**
 * Делегат для подсчёта обращений к свойству.
 * АКЦЕНТ: Можно собирать метрики использования.
 */
class AccessCountDelegate<T>(private var value: T) : ReadWriteProperty<Any?, T> {
    private var readCount = 0
    private var writeCount = 0
    
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        readCount++
        return value
    }
    
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        writeCount++
        this.value = value
    }
    
    fun getStats(): String = "Чтений: $readCount, Записей: $writeCount"
}

/**
 * Класс для демонстрации собственных делегатов.
 */
class CustomDelegatesDemo {
    var trackedValue: String by LoggingDelegate("начальное")
    var limitedValue: Int by LimitedDelegate(50, 0, 100)
    
    private val accessDelegate = AccessCountDelegate("test")
    var monitoredValue: String by accessDelegate
    
    fun showStats() {
        println("    Статистика monitoredValue: ${accessDelegate.getStats()}")
    }
}

// =============================================================================
// 4. АНОНИМНЫЕ ОБЪЕКТЫ (Object Expressions)
// =============================================================================

/**
 * Интерфейс для обработки событий клика.
 */
interface ClickListener {
    fun onClick()
    fun onLongClick()
}

/**
 * Установка слушателя через анонимный объект.
 * АКЦЕНТ: Анонимный объект создаётся прямо в месте использования.
 */
fun setupClickListener(listener: ClickListener) {
    println("  Вызов onClick:")
    listener.onClick()
    println("  Вызов onLongClick:")
    listener.onLongClick()
}

/**
 * Создание счётчика через анонимный объект.
 * АКЦЕНТ: Анонимный объект может захватывать переменные из внешней области.
 */
fun createCounter(): () -> Int {
    var count = 0
    return object : () -> Int {
        override fun invoke(): Int {
            return ++count
        }
    }
}

/**
 * Стратегия подсказок для игры.
 * АКЦЕНТ: Анонимные объекты удобны для реализации паттерна Стратегия.
 */
interface HintStrategy {
    fun giveHint(secret: Int, guess: Int): String
}

fun createHintStrategy(verbose: Boolean): HintStrategy {
    return if (verbose) {
        // Детальная стратегия
        object : HintStrategy {
            override fun giveHint(secret: Int, guess: Int): String {
                val diff = kotlin.math.abs(secret - guess)
                return when {
                    diff == 0 -> "Угадал!"
                    diff > 50 -> "Очень далеко! (разница > 50)"
                    diff > 20 -> "Далеко (разница > 20)"
                    diff > 10 -> "Близко (разница > 10)"
                    diff > 5 -> "Очень близко (разница > 5)"
                    else -> "Горячо! (разница ≤ 5)"
                }
            }
        }
    } else {
        // Простая стратегия
        object : HintStrategy {
            override fun giveHint(secret: Int, guess: Int): String {
                return when {
                    guess < secret -> "Больше"
                    guess > secret -> "Меньше"
                    else -> "Угадал!"
                }
            }
        }
    }
}

// =============================================================================
// 5. OBJECT DECLARATION (Singleton)
// =============================================================================

/**
 * Singleton через object declaration.
 * АКЦЕНТ: object создаёт единственный экземпляр класса (thread-safe).
 */
object GameConfig {
    const val MIN_VALUE = 0
    const val MAX_VALUE = 100
    const val MAX_ATTEMPTS = 10
    
    fun printConfig() {
        println("  Конфигурация игры: [$MIN_VALUE..$MAX_VALUE], попыток: $MAX_ATTEMPTS")
    }
}

/**
 * Companion object для фабричных методов.
 * АКЦЕНТ: Companion object — это именованный или безымянный синглтон внутри класса.
 */
class GameSession private constructor(val difficulty: String) {
    
    companion object Factory {
        fun easy() = GameSession("Легкий")
        fun normal() = GameSession("Нормальный")
        fun hard() = GameSession("Сложный")
    }
    
    fun start() {
        println("  Сессия начата: режим '$difficulty'")
    }
}

// =============================================================================
// ДЕМОНСТРАЦИОННАЯ ФУНКЦИЯ
// =============================================================================

fun demonstrateDelegation() {
    println("=== ДЕМОНСТРАЦИЯ DELEGATION ===\n")
    
    // 1. Делегирование класса
    println("1. Делегирование класса:")
    val remote = RemoteDataSource()
    val repo = Repository(remote)
    println("  Вызов repo.fetchData():")
    println("  Результат: ${repo.fetchData()}")
    println()
    
    // 2. Делегирование с переопределением
    println("2. Делегирование Logger:")
    val cachedLogger = CachedLogger(ConsoleLogger())
    cachedLogger.log("Первое сообщение")
    cachedLogger.log("Второе сообщение")
    cachedLogger.error("Ошибка")
    cachedLogger.showCache()
    println()
    
    // 3. Встроенные делегаты свойств
    println("3. Встроенные делегаты свойств:")
    val demo = PropertyDelegatesDemo()
    
    println("  3.1 lazy:")
    println("  Первое обращение к expensiveValue:")
    println("  Значение: ${demo.expensiveValue}")
    println("  Второе обращение к expensiveValue:")
    println("  Значение: ${demo.expensiveValue}")
    println()
    
    println("  3.2 observable:")
    demo.name = "Alice"
    demo.name = "Bob"
    println()
    
    println("  3.3 vetoable:")
    demo.age = 25
    println("    Возраст установлен: ${demo.age}")
    demo.age = -5
    println("    Возраст после отклонённой попытки: ${demo.age}")
    demo.age = 200
    println("    Возраст после отклонённой попытки: ${demo.age}")
    println()
    
    println("  3.4 notNull:")
    demo.score = 100
    println("    Score: ${demo.score}")
    println()
    
    // 4. Делегирование в Map
    println("4. Делегирование в Map:")
    val userMap = mapOf(
        "name" to "Alice",
        "age" to 30,
        "email" to "alice@example.com"
    )
    val user = UserFromMap(userMap)
    println("  $user")
    println()
    
    // 5. Собственные делегаты
    println("5. Собственные делегаты:")
    val customDemo = CustomDelegatesDemo()
    
    println("  5.1 LoggingDelegate:")
    customDemo.trackedValue = "новое значение"
    val value = customDemo.trackedValue
    println("    Прочитанное значение: $value")
    println()
    
    println("  5.2 LimitedDelegate:")
    customDemo.limitedValue = 50
    println("    Установлено: 50, значение: ${customDemo.limitedValue}")
    customDemo.limitedValue = 150
    println("    Установлено: 150, значение: ${customDemo.limitedValue}")
    customDemo.limitedValue = -10
    println("    Установлено: -10, значение: ${customDemo.limitedValue}")
    println()
    
    println("  5.3 AccessCountDelegate:")
    repeat(3) { customDemo.monitoredValue }
    customDemo.monitoredValue = "value1"
    customDemo.monitoredValue = "value2"
    customDemo.showStats()
    println()
    
    // 6. Анонимные объекты
    println("6. Анонимные объекты:")
    println("  6.1 ClickListener:")
    setupClickListener(object : ClickListener {
        override fun onClick() {
            println("    Обработка клика")
        }
        
        override fun onLongClick() {
            println("    Обработка длинного клика")
        }
    })
    println()
    
    println("  6.2 Счётчик:")
    val counter = createCounter()
    println("    ${counter()}, ${counter()}, ${counter()}")
    println()
    
    println("  6.3 Стратегия подсказок:")
    val verboseStrategy = createHintStrategy(verbose = true)
    val simpleStrategy = createHintStrategy(verbose = false)
    
    println("    Детальная стратегия (secret=50, guess=10):")
    println("    ${verboseStrategy.giveHint(50, 10)}")
    println("    Простая стратегия (secret=50, guess=10):")
    println("    ${simpleStrategy.giveHint(50, 10)}")
    println()
    
    // 7. Object declaration
    println("7. Object (Singleton):")
    GameConfig.printConfig()
    println()
    
    // 8. Companion object
    println("8. Companion object (Factory):")
    val session1 = GameSession.easy()
    val session2 = GameSession.hard()
    session1.start()
    session2.start()
}
