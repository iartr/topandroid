# Lecture 1.2 - Сложности, лимиты и история попыток

## Почему эта тема важна
Домашнее задание 1.2 расширяет игру угадывания: появляются уровни сложности, ограничение попыток и вывод истории. Эти элементы учат моделировать состояние, распределять ответственность между объектами и аккуратно работать со стандартной библиотекой Kotlin.

## Теория и ключевые конструкции
### Enum для уровней сложности
- `enum class Difficulty` собирает все режимы игры в одном месте и позволяет хранить параметры (например, лимит попыток). Подробнее: [Kotlin Docs - Enum Classes](https://kotlinlang.org/docs/enum-classes.html).

### Data class для хранения попыток
- `data class GuessRecord(val value: Int, val feedback: String)` автоматически генерирует `equals`, `hashCode`, `toString` и `copy`, что упрощает работу с коллекциями. Подробнее: [Kotlin Docs - Data Classes](https://kotlinlang.org/docs/data-classes.html).

### Коллекции и модификаторы доступа
- Историю удобно хранить в `MutableList<GuessRecord>` и дополнять через `history.add(GuessRecord(...))`. Обзор: [Kotlin Docs - Collections Overview](https://kotlinlang.org/docs/collections-overview.html).
- Вспомогательные функции (`askDifficulty`, `readGuess`) объявляйте `private`, чтобы явно ограничить область видимости. Детали: [Kotlin Docs - Visibility Modifiers](https://kotlinlang.org/docs/visibility-modifiers.html).

### Обработка состояний через when
- `when` по строкам и числам превращает выбор сложности и разбор результата в читаемый конечный автомат. Справочник: [Kotlin Docs - When Expression](https://kotlinlang.org/docs/control-flow.html#when-expression).

### joinToString и форматирование вывода
- Используйте `joinToString(separator = ", ")` для печати истории в человекочитаемом виде. Документация: [joinToString](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/join-to-string.html).

## Шаги решения домашнего задания
1. Определите `enum class Difficulty` и добавьте в него лимит попыток (`Int?`), чтобы `HARD` автоматически ограничивал игру.
2. Реализуйте `askDifficulty()` и вызывайте ее в `main`, пока пользователь не введет корректный ответ.
3. Соберите историю ходов в `MutableList<GuessRecord>`; добавляйте в список каждую успешную попытку после успешного парсинга.
4. Перед печатью подсказки уменьшайте счетчик оставшихся попыток в `HARD` и проверяйте, не опустился ли он до нуля.
5. По завершении раунда выведите сводку: количество попыток и их список через `joinToString`.

## Типичные ошибки и как их избежать
- Жестко прошитый лимит попыток вне `Difficulty`: храните правило рядом с режимом, чтобы не дублировать значения.
- Добавление `null` в историю из-за пропущенной проверки `toIntOrNull()`. Всегда фильтруйте нечисловой ввод до сохранения.
- Неправильное завершение игры в `HARD`: проверяйте лимит после добавления попытки, иначе потеряете последнюю подсказку.

## Самопроверка
- Запустите игру и убедитесь, что `HARD` заканчивается через 7 неудачных попыток.
- Проверьте, что история печатается в порядке ввода и очищается после рестарта.
- Попробуйте сменить режим на `EASY`: лимит должен отключиться без дополнительных правок.
- Введите, например, `abc` и подтвердите, что ошибка выводится и история не меняется.

## Дополнительные материалы
- [Enum Classes - Kotlin Docs](https://kotlinlang.org/docs/enum-classes.html)
- [Data Classes - Kotlin Docs](https://kotlinlang.org/docs/data-classes.html)
- [Collections Overview - Kotlin Docs](https://kotlinlang.org/docs/collections-overview.html)
- [Visibility Modifiers - Kotlin Docs](https://kotlinlang.org/docs/visibility-modifiers.html)
- [joinToString - Kotlin Standard Library](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/join-to-string.html)
