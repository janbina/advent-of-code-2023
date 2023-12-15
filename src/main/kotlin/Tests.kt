import kotlin.system.measureTimeMillis

fun testAll() {
    println("running tests..")
    val time = measureTimeMillis {
        expectedResults.forEach { (dayNo, result) ->
            val dayTime = measureTimeMillis {
                val input = getDayInputFile(dayNo).getOrThrow().bufferedReader()
                val day = createDay(dayNo, input)
                print("day ${dayNo.toString().padStart(2, '0')}")
                result.p1?.let {
                    require(it == day.solvePart1())
                    print(" p1✓")
                }
                result.p2?.let {
                    require(it == day.solvePart2())
                    print(" p2✓")
                }
            }
            println(" $dayTime ms")
        }
    }
    println("tests ok! $time ms")
}

private val expectedResults = sortedMapOf(
    1 to Result(56049, 54530),
    2 to Result(2164, 69929),
    3 to Result(512794, 67779080),
    4 to Result(20667, 5833065),
    5 to Result(382895070L, 17729182L),
    6 to Result(4568778L, 28973936L),
    7 to Result(253313241, 253362743),
    8 to Result(19783, 9177460370549L),
    9 to Result(1955513104, 1131),
    10 to Result(6907, 541),
    11 to Result(9536038L, 447744640566L),
)

private class Result(
    val p1: Any?,
    val p2: Any?,
)
