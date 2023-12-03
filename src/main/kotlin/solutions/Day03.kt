package solutions

import utils.product
import utils.sumOf
import java.io.BufferedReader

class Day03(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input = inputReader.transformLines { it }

    private fun List<String>.safeGet(x: Int, y: Int): Char {
        if (y !in this.indices) return '.'
        val line = this[y]
        if (x !in line.indices) return '.'
        return line[x]
    }

    private fun Char.isSymbol(): Boolean {
        return !isDigit() && this != '.'
    }

    private fun Char.isGear(): Boolean {
        return this == '*'
    }

    // returns number starting on index [x,y] or null
    private fun getNumberOnIndex(x: Int, y: Int): String? {
        var num = ""
        var ix = x
        while (true) {
            val next = input.safeGet(ix++, y)
            if (next.isDigit()) {
                num += next
            } else {
                return num.ifBlank { null }
            }
        }
    }

    private fun hasAdjacentSymbol(x: Int, y: Int, digitCount: Int): Boolean {
        for (ix in x - 1..x + digitCount) {
            for (iy in y - 1..y + 1) {
                if (input.safeGet(ix, iy).isSymbol()) {
                    return true
                }
            }
        }
        return false
    }

    private fun getGears(x: Int, y: Int, digitCount: Int): List<Pair<Int, Int>> {
        val gears = mutableListOf<Pair<Int, Int>>()
        for (ix in x - 1..x + digitCount) {
            for (iy in y - 1..y + 1) {
                if (input.safeGet(ix, iy).isGear()) {
                    gears += ix to iy
                }
            }
        }
        return gears
    }

    override fun solvePart1(): Int {
        var sum = 0

        for (iy in input.indices) {
            var ix = 0
            while (ix <= input[iy].lastIndex) {
                val num = getNumberOnIndex(ix, iy)
                if (num != null && hasAdjacentSymbol(ix, iy, num.length)) {
                    sum += num.toInt()
                }
                ix += num?.length ?: 1
            }
        }

        return sum
    }

    override fun solvePart2(): Int {
        // maps gear index to adjacent numbers
        val gearToNums = mutableMapOf<Pair<Int, Int>, List<Int>>()

        for (iy in input.indices) {
            var ix = 0
            while (ix <= input[iy].lastIndex) {
                val num = getNumberOnIndex(ix, iy)
                if (num != null) {
                    getGears(ix, iy, num.length).forEach { gear ->
                        gearToNums.merge(gear, listOf(num.toInt()), Collection<Int>::plus)
                    }
                }
                ix += num?.length ?: 1
            }
        }

        return gearToNums
            .filterValues { it.size == 2 }
            .sumOf { it.value.product().toInt() }
    }
}
