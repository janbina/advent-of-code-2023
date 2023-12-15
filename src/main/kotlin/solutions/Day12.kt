package solutions

import java.io.BufferedReader

class Day12(
    inputReader: BufferedReader,
) : Day<Long, Long>() {

    private data class Spring(
        val value: String,
        val counts: List<Int>,
    ) {
        // string template for the counts:
        // '.' means operational spring required
        // '-' means nothing or unlimited number of operational springs
        // '#' means damaged spring required
        val template = counts.joinToString(
            separator = ".-",
            prefix = "-",
            postfix = "-",
            transform = { "#".repeat(it) },
        )

        val templateCounts = buildCounts(template) { it == '#' }
        val valueAllCounts = buildCounts(value) { it == '#' || it == '?' }
        val valueRequiredCounts = buildCounts(value) { it == '#' }

        private fun buildCounts(source: String, matcher: (Char) -> Boolean): List<Int> {
            val arr = Array(source.length) { 0 }
            for (i in source.lastIndex downTo 0) {
                arr[i] = (arr.getOrNull(i + 1) ?: 0) + if (matcher(source[i])) 1 else 0
            }
            return arr.toList()
        }
    }

    private fun Spring.unfolded(times: Int): Spring {
        return Spring(
            value = (0..<times).joinToString(separator = "?") { value },
            counts = (0..<times).flatMap { counts },
        )
    }

    private val input = inputReader.transformLines { line ->
        val (map, nums) = line.split(" ")
        val counts = nums.split(",").map { it.toInt() }
        Spring(map, counts)
    }

    override fun solvePart1(): Long {
        return input.sumOf { solve(it) }
    }

    override fun solvePart2(): Long {
        return input.sumOf { solve(it.unfolded(5)) }
    }

    private fun solve(
        spring: Spring,
        valueIndex: Int = 0,
        templateIndex: Int = 0,
        cache: MutableMap<Pair<Int, Int>, Long> = mutableMapOf(),
    ): Long {
        cache[valueIndex to templateIndex]?.let { return it }
        if (valueIndex > spring.value.lastIndex && templateIndex > spring.template.lastIndex) {
            return 1 // both index out, we matched the template
        }
        if (valueIndex > spring.value.lastIndex) {
            return if (spring.templateCounts[templateIndex] == 0) 1 else 0
        }
        if (templateIndex > spring.template.lastIndex) {
            return if (spring.valueRequiredCounts[valueIndex] == 0) 1 else 0
        }
        if (spring.templateCounts[templateIndex] > spring.valueAllCounts[valueIndex]) {
            return 0
        }
        if (spring.valueRequiredCounts[valueIndex] > spring.templateCounts[templateIndex]) {
            return 0
        }
        if (spring.templateCounts[templateIndex] == 0 && spring.valueRequiredCounts[valueIndex] == 0) {
            return 1
        }

        val vChar = spring.value[valueIndex]
        val tChar = spring.template[templateIndex]

        val nextIndices: List<Pair<Int, Int>> = when {
            vChar == '#' && tChar == '#' -> listOf(valueIndex + 1 to templateIndex + 1)
            vChar == '#' && tChar == '.' -> emptyList()
            vChar == '#' && tChar == '-' -> listOf(valueIndex to templateIndex + 1)
            vChar == '.' && tChar == '#' -> emptyList()
            vChar == '.' && tChar == '.' -> listOf(valueIndex + 1 to templateIndex + 1)
            vChar == '.' && tChar == '-' -> listOf(valueIndex + 1 to templateIndex)
            vChar == '?' && tChar == '#' -> listOf(valueIndex + 1 to templateIndex + 1)
            vChar == '?' && tChar == '.' -> listOf(valueIndex + 1 to templateIndex + 1)
            vChar == '?' && tChar == '-' -> listOf(
                valueIndex + 1 to templateIndex,
                valueIndex to templateIndex + 1,
            )

            else -> emptyList()
        }

        return nextIndices.sumOf { (vIndex, tIndex) ->
            solve(spring, vIndex, tIndex, cache).also { cache[vIndex to tIndex] = it }
        }
    }
}
