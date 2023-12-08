
fun testAll() {
    println("running tests..")
    expectedResults.forEach { (dayNo, result) ->
        val input = getDayInputFile(dayNo).getOrThrow().bufferedReader()
        val day = createDay(dayNo, input)
        print("day $dayNo")
        result.p1?.let {
            require(it == day.solvePart1())
            print(" p1✓")
        }
        result.p2?.let {
            require(it == day.solvePart2())
            print(" p2✓")
        }
        println()
    }
    println("tests ok!")
}

private val expectedResults = sortedMapOf(
    1 to Result(56049, 54530),
    2 to Result(2164, 69929),
    3 to Result(512794, 67779080),
    4 to Result(20667, 5833065),
    5 to Result(382895070L, 17729182L),
)

private class Result(
    val p1: Any?,
    val p2: Any?,
)
