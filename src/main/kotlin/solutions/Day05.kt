package solutions

import java.io.BufferedReader

class Day05(
    inputReader: BufferedReader,
) : Day<Long, Long>() {

    private val lines = inputReader.transformLines { it }
    private val seeds = lines.first().removePrefix("seeds: ").split(" ").map { it.toLong() }
    private val seeds2 = seeds.chunked(2).map { it.first() until it.first() + it.last() }
    private val componentMaps = lines.filter { it.endsWith("map:") }.map { line ->
        val (source, dest) = line.removeSuffix(" map:").split("-to-")
        val rangeMap = lines.dropWhile { it != line }.drop(1).takeWhile { it.isNotEmpty() }
            .map { rangeLine ->
                val (destStart, sourceStart, size) = rangeLine.split(" ").map { it.toLong() }
                RangeMap(
                    source = sourceStart until sourceStart + size,
                    dest = destStart until destStart + size,
                )
            }
        ComponentMap(source = source, dest = dest, ranges = rangeMap)
    }

    override fun solvePart1(): Long {
        return getMinLocation(seeds.map { it..it })
    }

    override fun solvePart2(): Long {
        return getMinLocation(seeds2)
    }

    private fun getMinLocation(seeds: List<LongRange>): Long {
        var currentName = "seed"
        var currentIntervals = seeds
        while (currentName != "location") {
            val map = componentMaps.first { it.source == currentName }
            currentName = map.dest
            currentIntervals = mapIntervals(currentIntervals, map.ranges)
        }
        return currentIntervals.minOf { it.first }
    }

    private fun mapIntervals(
        from: List<LongRange>,
        to: List<RangeMap>,
    ): List<LongRange> {
        return buildList {
            from.forEach { interval ->
                var lo = interval.first
                while (lo <= interval.last) {
                    val dest = to.firstOrNull { lo in it.source }
                    if (dest != null) { // maping to dest
                        val end = interval.last.coerceAtMost(dest.source.last)
                        add(dest.mapped(lo)..dest.mapped(end))
                        lo = end + 1
                    } else { // mapping to same number
                        val firstHigher = to.filter { it.source.first > lo }.minByOrNull { it.source.first }
                        val limit = firstHigher?.source?.first?.minus(1) ?: Long.MAX_VALUE
                        val end = interval.last.coerceAtMost(limit)
                        add(lo..end)
                        lo = end + 1
                    }
                }
            }
        }
    }
}

private data class RangeMap(
    val source: LongRange,
    val dest: LongRange,
) {
    fun mapped(sourceId: Long): Long {
        return dest.first + sourceId - source.first
    }
}

private data class ComponentMap(
    val source: String,
    val dest: String,
    val ranges: List<RangeMap>,
)
