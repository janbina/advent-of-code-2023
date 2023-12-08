package solutions

import utils.productOf
import utils.splitOnWhitespace
import java.io.BufferedReader
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

class Day06(
    inputReader: BufferedReader,
) : Day<Long, Long>() {

    private val input = parseInput(inputReader.transformLines { it })

    override fun solvePart1(): Long {
        return input.races.productOf { it.errorMargin() }
    }

    override fun solvePart2(): Long {
        return input.masterRace.errorMargin()
    }

    private fun Race.errorMargin(): Long {
        val d = sqrt(time * time - 4 * distanceRecord.toDouble())
        val t1 = ceil((time - d) / 2).toLong()
        val t2 = floor((time + d) / 2).toLong()
        return t2 - t1 + 1
    }

    private fun parseInput(input: List<String>): Input06 {
        val times = input[0].splitOnWhitespace().drop(1).map { it.toLong() }
        val dists = input[1].splitOnWhitespace().drop(1).map { it.toLong() }
        require(times.size == dists.size)
        val races = times.mapIndexed { i, time ->
            Race(time = time, distanceRecord = dists[i])
        }
        val masterRace = Race(
            time = input[0].filter { it.isDigit() }.toLong(),
            distanceRecord = input[1].filter { it.isDigit() }.toLong(),
        )
        return Input06(races, masterRace)
    }
}

private class Input06(
    val races: List<Race>,
    val masterRace: Race,
)

private data class Race(
    val time: Long,
    val distanceRecord: Long,
)
