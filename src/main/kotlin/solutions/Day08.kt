package solutions

import utils.lcm
import java.io.BufferedReader

class Day08(
    inputReader: BufferedReader,
) : Day<Int, Long>() {

    private val input = inputReader.transformLines { it }
    private val moves = input.first().trim()
    private val graph = input.drop(2).filter { it.isNotBlank() }.associate { line ->
        val f = line.substring(0, 3)
        val l = line.substring(7, 10)
        val r = line.substring(12, 15)
        f to (l to r)
    }

    private fun getNext(from: String, moveIndex: Int): String {
        return when (val move = moves[moveIndex % moves.length]) {
            'L' -> graph[from]?.first ?: error("no directions for $from")
            'R' -> graph[from]?.second ?: error("no directions for $from")
            else -> error("Illegal move $move")
        }
    }

    private fun getDistance(from: String, toCondition: (String) -> Boolean): Int {
        var i = 0
        var current = from
        while (!toCondition(current)) {
            current = getNext(current, i++)
        }
        return i
    }

    override fun solvePart1(): Int {
        return getDistance(from = "AAA", toCondition = { it == "ZZZ" })
    }

    override fun solvePart2(): Long {
        val starts = graph.keys.filter { it.endsWith('A') }
        val dists = starts.map { from ->
            getDistance(from = from, toCondition = { it.endsWith('Z') }).toLong()
        }
        return lcm(dists)
    }
}
