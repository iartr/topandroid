# Lecture 1.3 - Лямбда-форматтер и History<T>

## Почему эта тема важна
Домашнее задание 1.3 завершает переработку консольной игры: вы переносите форматирование в функцию высшего порядка, описываете результат через `sealed class` и создаете универсальную коллекцию `History<T>`. Эти приемы укрепляют понимание архитектуры и подготавливают к большим проектам.

## Теория и ключевые конструкции
### Лямбда-форматтер и функции высшего порядка
- Передавайте логику вывода как параметр: `val formatter: (GuessResult, Int?) -> String`. Подробнее: [Kotlin Docs - Lambda Expressions](https://kotlinlang.org/docs/lambdas.html).
- Для читаемости объявите `typealias ResultFormatter = (GuessResult, Int?) -> String`. Документация: [Kotlin Docs - Type Aliases](https://kotlinlang.org/docs/type-aliases.html).

### Sealed class для результата
- `sealed class GuessResult` гарантирует, что вы перебрали все варианты в `when`. Используйте вложенные `data class` для передачи дополнительной информации. Подробнее: [Kotlin Docs - Sealed Classes](https://kotlinlang.org/docs/sealed-classes.html).

### Дженерики и собственные коллекции
- `class History<T>` демонстрирует параметрический полиморфизм: коллекция работает с любыми типами. Обзор: [Kotlin Docs - Generics](https://kotlinlang.org/docs/generics.html).

### Интерфейсы Iterable и Iterator
- Реализуйте `Iterable<T>` и переопределите `iterator()`, чтобы `History` работала с `for` и `joinToString`. Подробности: [Iterable - Kotlin Standard Library](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/).

### Разделение логики и форматирования
- Благодаря внедрению форматтера игровая логика остается чистой, а вывод можно менять без переписывания движка. Почитайте про разделение ответственностей: [Android Architecture Guidelines](https://developer.android.com/topic/architecture).

## Шаги решения домашнего задания
1. Опишите `sealed class GuessResult` с вариантами `Higher`, `Lower`, `Correct` и, при необходимости, `Invalid`.
2. Объявите `typealias ResultFormatter` и передавайте лямбду в движок игры или цикл.
3. Реализуйте `History<T>`: храните данные в приватном `MutableList`, возвращайте копию в `toList()` и реализуйте `iterator()`.
4. Обновите игровой цикл: при каждом валидном угадывании добавляйте элемент в историю и вызывайте форматтер.
5. Сформируйте итоговую сводку: количество попыток, список значений, возможно, среднее или минимум, используя методы коллекций.

## Типичные ошибки и как их избежать
- Возврат внутреннего списка из `History`: всегда отдавайте копию, иначе потребители смогут мутировать состояние.
- Обработка `GuessResult.Invalid` внутри игрового движка: не уменьшайте счетчик попыток для нечислового ввода.
- Слишком сложный форматтер: если ветви `when` громоздкие, вынесите их в отдельные частные функции или используйте несколько функций форматирования.

## Самопроверка
- Подставьте фиктивный форматтер, который пишет JSON, и убедитесь, что игровой цикл не меняется.
- Проверьте, что по завершении игры `History` возвращает неизменяемый список, а исходная коллекция недоступна извне.
- Добавьте новый результат (например, `TooClose`) и убедитесь, что компилятор потребовал обновить `when` во всех местах.
- Запустите игру с жестким лимитом и проверьте, что сообщения форматтера зависят от оставшихся попыток.

## Дополнительные материалы
- [Lambda Expressions - Kotlin Docs](https://kotlinlang.org/docs/lambdas.html)
- [Type Aliases - Kotlin Docs](https://kotlinlang.org/docs/type-aliases.html)
- [Sealed Classes - Kotlin Docs](https://kotlinlang.org/docs/sealed-classes.html)
- [Generics - Kotlin Docs](https://kotlinlang.org/docs/generics.html)
- [Iterable - Kotlin Standard Library](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/)
- [Guide to App Architecture - Android Developers](https://developer.android.com/topic/architecture)
