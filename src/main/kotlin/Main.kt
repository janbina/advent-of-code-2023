import java.time.Month
import java.time.OffsetDateTime
import java.time.ZoneId
import kotlin.system.measureTimeMillis

const val YEAR = 2023

fun main() {
    measureTimeMillis {
        runDay(getCurrentDay())
    }.also {
        println("time taken: $it")
    }
    testAll()
}

private fun getCurrentDay(): Int {
    // puzzles open at midnight US Eastern Time
    val date = OffsetDateTime.now(ZoneId.of("US/Eastern"))
    return if (date.month == Month.DECEMBER) {
        date.dayOfMonth.coerceAtMost(25)
    } else 25
}

private fun runDay(
    dayNumber: Int,
) {
    val inputReader = getDayInputFile(dayNumber).getOrThrow().bufferedReader()
    val day = createDay(dayNumber, inputReader)
    println("solving day $dayNumber")
    println("\tpart 1 = ${day.solvePart1()}")
    println("\tpart 2 = ${day.solvePart2()}")
}
