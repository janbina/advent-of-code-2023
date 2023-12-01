
fun testAll() {
    expectedResults.forEach { (dayNo, result) ->
        val input = getDayInputFile(dayNo).getOrThrow().bufferedReader()
        val day = createDay(dayNo, input)
        result.p1?.let {
            require(it == day.solvePart1())
        }
        result.p2?.let {
            require(it == day.solvePart2())
        }
    }
}

private val expectedResults = sortedMapOf(
    1 to Result(56049, 54530),
)

private class Result(
    val p1: Any?,
    val p2: Any?,
)
