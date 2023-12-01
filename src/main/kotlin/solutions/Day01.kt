package solutions

import utils.indexOfOrNull
import java.io.BufferedReader

class Day01(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input = inputReader.transformLines { it }

    private val digits = (0..9).map { "$it" to it }
    private val words = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    ).mapIndexed { i, s -> s to i + 1 }

    private fun String.calibrationValue(
        nums: List<Pair<String, Int>>,
    ): Int {
        require(nums.isNotEmpty())
        val first = nums.minByOrNull { (string, _) ->
            indexOfOrNull(string) ?: Int.MAX_VALUE
        }!!
        val second = nums.maxByOrNull { (string, _) ->
            lastIndexOf(string)
        }!!
        return first.second * 10 + second.second
    }

    override fun solvePart1(): Int {
        val nums = digits
        return input.sumOf { it.calibrationValue(nums) }
    }

    override fun solvePart2(): Int {
        val nums = digits + words
        return input.sumOf { it.calibrationValue(nums) }
    }
}
