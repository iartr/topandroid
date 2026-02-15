package homework1_1

import kotlin.random.Random

// Цель: собрать минимальную игру с одним файлом base_project.main и логикой сравнения.
// Подсказки и статистика не требуются. См. "homework 1.1.md".
// Оставлены TODO, чтобы студент потренировался.

private const val APP_NAME: String = "Guess 0..100"
fun main() {
    println("=== $APP_NAME ===")
    println("Команды: help | exit | stats")
    val randomNumber = Random.nextInt(100)

    println("Игра 0..100 — минимальная версия")

    while (true) {
        println("Введи число")
        val input = readlnOrNull()?.trim()

        when {
            input == null || input.isEmpty() -> {
                println("Пожалуйста, введите число или команду")
                continue
            }
            input.trim().equals("exit", ignoreCase = true) -> {
                println("EOF.До встречи!")
                break
            }
            input.trim().equals("help", ignoreCase = true) -> {
                println("Команды: help - справка, exit - выход")
                continue
            }
            else -> {
                val guess = input.toIntOrNull()
                if (guess == null) {
                    println("Ошибка: введите число или команду")
                    continue
                }
                when {
                    guess < randomNumber -> println("Мое число больше")
                    guess > randomNumber -> println("Мое число меньше")
                    else -> {
                        println("Угадал!)")
                        break
                    }
                }
            }
        }
    }
}


// TODO: 1) загадать число
// TODO: 2) цикл while(true): читать ввод (readlnOrNull), парсить в Int?
// TODO: 3) сравнивать и печатать: больше/меньше/угадал
// TODO: 4) обработать команды: exit, help


