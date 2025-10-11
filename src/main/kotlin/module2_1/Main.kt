package module2_1

/**
 * ГЛАВНАЯ ДЕМОНСТРАЦИОННАЯ ПРОГРАММА - Модуль 2.1
 * 
 * Запустите эту функцию для демонстрации всех возможностей модуля:
 * - Extension Functions
 * - Inline Functions (inline, reified, noinline, crossinline)
 * - Delegation (by keyword, delegated properties, анонимные объекты)
 *
 * 1. Запустите эту программу и покажите результаты
 * 2. Откройте любой файл и посмотрите байткод: Tools → Kotlin → Show Kotlin Bytecode → Decompile
 */
fun main() {
    println("╔═══════════════════════════════════════════════════════════════╗")
    println("║  МОДУЛЬ 2.1: ПРОДВИНУТЫЙ KOTLIN                              ║")
    println("║  Темы: Extensions, Inline, Reified, Delegation               ║")
    println("╚═══════════════════════════════════════════════════════════════╝")
    println()
    
    // Демонстрация Extension Functions
    demonstrateExtensions()
    println("\n" + "=".repeat(65) + "\n")
    
    // Демонстрация Inline Functions
    demonstrateInlineFunctions()
    println("\n" + "=".repeat(65) + "\n")
    
    // Демонстрация Delegation
    demonstrateDelegation()
    println("\n" + "=".repeat(65) + "\n")
    
    // Интерактивная часть: мини-игра с применением концепций
    println("=== ПРАКТИЧЕСКОЕ ПРИМЕНЕНИЕ: УЛУЧШЕННАЯ ИГРА ===\n")
    playEnhancedGame()
    
    println("\n╔═══════════════════════════════════════════════════════════════╗")
    println("║  ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА                                       ║")
    println("║                                                               ║")
    println("║  Следующий шаг:                                               ║")
    println("║  1. Изучите байткод (Tools → Kotlin → Show Kotlin Bytecode)  ║")
    println("║  2. Выполните домашнее задание в homework2_1/                 ║")
    println("║  3. Экспериментируйте с кодом!                                ║")
    println("╚═══════════════════════════════════════════════════════════════╝")
}

/**
 * Мини-игра, демонстрирующая применение всех изученных концепций.
 * 
 * Применённые концепции:
 * - Extension functions для работы со списками и строками
 * - Inline functions для измерения производительности
 * - Reified для type-safe коллекций
 * - Delegated properties для отслеживания состояния
 * - Анонимные объекты для стратегий
 */
fun playEnhancedGame() {
    // Используем делегированное свойство с observable
    class GameState {
        var attempts: Int by kotlin.properties.Delegates.observable(0) { _, _, new ->
            if (new <= 3) {
                println("  💡 Подсказка: у вас осталось ${10 - new} попыток")
            }
        }
        
        // Lazy инициализация секретного числа
        val secret: Int by lazy {
            println("  🎲 Загадываю число...")
            (1..100).random()
        }
    }
    
    val state = GameState()
    val history = mutableListOf<Int>()
    
    // Анонимный объект для стратегии подсказок
    val hintStrategy = createHintStrategy(verbose = true)
    
    println("  Игра 'Угадай число' с продвинутыми возможностями Kotlin!")
    println("  Я загадал число от 1 до 100. У вас 10 попыток.\n")
    
    // Измеряем время игры с inline функцией
    val gameTime = measureTimeMillis {
        // Симуляция игры с предопределёнными попытками
        val testGuesses = listOf(50, 75, 62, 68, 70, 71)
        
        for (guess in testGuesses) {
            state.attempts++
            history.add(guess)
            
            print("  Попытка ${state.attempts}: $guess → ")
            val hint = hintStrategy.giveHint(state.secret, guess)
            println(hint)
            
            if (guess == state.secret) {
                println("\n  🎉 Вы угадали число ${state.secret}!")
                break
            }
            
            if (state.attempts >= 10) {
                println("\n  😔 Попытки закончились. Было загадано: ${state.secret}")
                break
            }
        }
    }
    
    // Используем extension functions для статистики
    println("\n  📊 Статистика игры:")
    println("  ${history.formatAsAttempts()}")
    println("  Среднее значение попыток: ${history.averageOrNull()?.let { "%.1f".format(it) } ?: "N/A"}")
    println("  Последние 3 попытки: ${history.lastN(3)}")
    println("  Время игры: $gameTime мс")
    
    // Демонстрация reified: фильтрация смешанного списка
    val mixedData: List<Any> = listOf(1, "test", 42, "data", history, 3.14)
    val numbersOnly = mixedData.filterByType<Int>()
    println("\n  🔍 Reified в действии:")
    println("  Смешанные данные: $mixedData")
    println("  Только числа: $numbersOnly")
}
