package solutions

import utils.splitOnWhitespace
import java.io.BufferedReader
import java.lang.IllegalArgumentException

class Day09(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input = inputReader.transformLines { line ->
        line.splitOnWhitespace().map { it.toInt() }
    }

    override fun solvePart1(): Int {
        return input.sumOf { seq ->
            seq.buildSequences().sumOf { it.last() }
        }
    }

    override fun solvePart2(): Int {
        return input.sumOf { seq ->
            seq.buildSequences().mapIndexed { i, ints ->
                ints.first() * if (i % 2 == 0) 1 else -1
            }.sum()
        }
    }

    private fun List<Int>.buildSequences(): List<List<Int>> {
        var current = this
        return buildList {
            add(current)
            while (current.any { it != 0 }) {
                current = current.diffSequence()
                add(current)
            }
        }
    }

    private fun List<Int>.diffSequence(): List<Int> {
        if (size < 2) throw IllegalArgumentException("cannot make diff seq from list size < 2")
        return windowed(size = 2, step = 1, partialWindows = false)
            .map { it[1] - it[0] }
    }
}
