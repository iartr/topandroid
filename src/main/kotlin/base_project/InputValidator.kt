package base_project

object InputValidator {
    // Nullable-возврат подталкивает к корректной обработке
    fun tryParseInt(raw: String): Int? = raw.toIntOrNull()
}