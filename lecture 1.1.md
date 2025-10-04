# Lecture 1.1 — Минимальная игра 0..100

## Почему эта тема важна
Эта лекция закладывает фундамент синтаксиса Kotlin и показывает полный цикл разработки: постановка задачи, разбор требований, черновой план, реализация и проверка. После изучения материала вы сможете самостоятельно решить домашнее задание 1.1 и получить рабочую консольную игру угадывания числа.

## Теория и ключевые конструкции
### Переменные и типы
- `val` объявляет неизменяемые значения, `var` — изменяемые. Подробнее: [Kotlin Docs - Basic Syntax: Variables](https://kotlinlang.org/docs/basic-syntax.html#variables).
- Тип можно не указывать, если компилятор может его вывести: `val secret = 42`. Подробнее: [Kotlin Docs - Type Inference](https://kotlinlang.org/docs/basic-syntax.html#variable-types).

### Чтение из консоли
- `readln()` бросает исключение при EOF, поэтому используем `readlnOrNull()` и проверяем `null`. Подробнее: [Kotlin Docs - Input and Output](https://kotlinlang.org/docs/input-output.html).
- Метод `trim()` удаляет пробелы по краям строки. Документация: [kotlin.text.trim](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/trim.html).

### Условия и ветвления
- `if/else` подходит для сравнения введённого числа с секретом. Подробнее: [Kotlin Docs - if Expression](https://kotlinlang.org/docs/control-flow.html#if-expression).
- `when (value)` удобен, когда нужно обрабатывать несколько команд (`exit`, `help`, пустой ввод). Подробнее: [Kotlin Docs - when Expression](https://kotlinlang.org/docs/control-flow.html#when-expression).

### Циклы
- `while (true)` создаёт бесконечный цикл. Из него выходим через `break` или `return`. Подробнее: [Kotlin Docs - while Loops](https://kotlinlang.org/docs/control-flow.html#while-loops).
- Внутри цикла важно иметь путь завершения, иначе программа зависнет. Подробнее о выходах из циклов: [Kotlin Docs - Returns and Jumps](https://kotlinlang.org/docs/returns.html).

### Диапазоны и случайные числа
- Диапазон `0..100` создаёт последовательность целых чисел. Обзор: [Kotlin Docs - Ranges](https://kotlinlang.org/docs/ranges.html).
- `Random.nextInt(0, 101)` или `(0..100).random()` возвращает число из диапазона. Документация: [Kotlin Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/).

## Шаги решения домашнего задания
1. **Инициализация игры**
   - Подключите `kotlin.random.Random` (или используйте готовую функцию `random()` у диапазона).
   - Создайте переменную `val secret = (0..100).random()` и счётчик `var attempts = 0`.
2. **Организация игрового цикла**
   - Напечатайте приветствие и подсказку по командам.
   - Запустите `while (true)` и в начале каждой итерации увеличивайте `attempts` после успешного чтения числа.
3. **Обработка пользовательского ввода**
   - Считайте строку: `val line = readlnOrNull()?.trim()`.
   - Если `line == null`, выводите «EOF. До встречи!» и `return`.
   - Реализуйте команды `exit` и `help`. Для `help` выведите краткую справку и переходите к следующей итерации `continue`.
   - Если строка пустая, просто продолжайте цикл.
4. **Парсинг числа**
   - Используйте `val guess = line.toIntOrNull()`.
   - При `guess == null` сообщите об ошибке и переходите к следующему шагу.
5. **Сравнение и вывод результата**
   - `when { guess < secret -> println("Моё число больше") ... }`.
   - Если угадали, сообщите количество попыток и выполните `return`.

## Самопроверка
- Попробуйте ввести пустую строку, некорректное число, `help`, `exit`, затем корректные попытки.
- Измените диапазон на `0..10` и убедитесь, что игра работает без изменений в логике.
- Подсчитайте количество попыток — вывод должен совпадать с фактическим числом вводов.

## Дополнительные материалы
- [Basic Syntax - Kotlin Docs](https://kotlinlang.org/docs/basic-syntax.html)
- [Input and Output - Kotlin Docs](https://kotlinlang.org/docs/input-output.html)
- [Null Safety - Kotlin Docs](https://kotlinlang.org/docs/null-safety.html)
- [Control Flow - Kotlin Docs](https://kotlinlang.org/docs/control-flow.html)
- [Ranges - Kotlin Docs](https://kotlinlang.org/docs/ranges.html)
- [Random API - Kotlin Standard Library](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/)
