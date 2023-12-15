package solutions

import utils.Point2D
import utils.uniquePairs
import java.io.BufferedReader

class Day11(
    inputReader: BufferedReader,
) : Day<Long, Long>() {

    private val input = inputReader.transformLines { it }
    private val emptyRows = input.indices.filter { rI -> input[rI].all { it == '.' } }
    private val emptyCols = input[0].indices.filter { cI -> input.all { it[cI] == '.' } }
    private val hashes = buildList {
        for (y in input.indices) {
            for (x in input.first().indices) {
                if (input[y][x] == '#') {
                    add(Point2D(x, y))
                }
            }
        }
    }

    override fun solvePart1(): Long {
        return countDistances(1)
    }

    override fun solvePart2(): Long {
        return countDistances(999_999)
    }

    private fun countDistances(multiplier: Int): Long {
        return hashes.map { p ->
            Point2D(
                x = p.x + emptyCols.count { it < p.x } * multiplier,
                y = p.y + emptyRows.count { it < p.y } * multiplier,
            )
        }.uniquePairs().sumOf { (a, b) -> a.distanceTo(b).toLong() }
    }
}
