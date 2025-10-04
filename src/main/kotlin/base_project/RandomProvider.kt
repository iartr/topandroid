package base_project

import kotlin.random.Random
import java.util.Random as JavaRandom

// Интерфейс для демонстрации DI + подмены генератора (например, в тестах)
interface RandomProvider {
    fun nextInt(min: Int, max: Int): Int
}

// Kotlin Random
class KotlinRandomProvider(
    private val rnd: Random = Random.Default
) : RandomProvider {
    override fun nextInt(min: Int, max: Int): Int {
        // until — исключая верхнюю границу, поэтому +1
        return rnd.nextInt(from = min, until = max + 1)
    }
}

// Java interop: базовый java.util.Random
class JavaUtilRandomProvider(
    private val rnd: JavaRandom = JavaRandom()
) : RandomProvider {
    override fun nextInt(min: Int, max: Int): Int {
        return min + rnd.nextInt(max - min + 1)
    }
}