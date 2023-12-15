package solutions

import utils.deepCopy
import java.io.BufferedReader

private const val RoundStone = 'O'
private const val BlockStone = '#'
private const val EmptySpace = '.'

class Day14(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input = inputReader.transformLines { it }
        .map { it.toCharArray().toTypedArray() }.toTypedArray()

    override fun solvePart1(): Int {
        val arr = input.deepCopy()
        tilt(arr, dx = 0, dy = -1)
        return calculateLoad(arr)
    }

    override fun solvePart2(): Int {
        val arr = input.deepCopy()
        val hashCodes = mutableMapOf<Int, Int>()
        var currentCycle = 0
        val numCycles = 1_000_000_000 - 1

        while (currentCycle < numCycles) {
            tilt(arr, dx = 0, dy = -1)
            tilt(arr, dx = -1, dy = 0)
            tilt(arr, dx = 0, dy = 1)
            tilt(arr, dx = 1, dy = 0)
            val hash = arr.contentDeepHashCode()

            val prevCycle = hashCodes[hash]
            val newCycle = if (prevCycle != null) {
                hashCodes.clear()
                val cycleLength = currentCycle - prevCycle
                val remaining = numCycles - currentCycle

                if (cycleLength > remaining) {
                    currentCycle + 1
                } else {
                    currentCycle + remaining / cycleLength * cycleLength
                }
            } else {
                currentCycle + 1
            }

            hashCodes[hash] = currentCycle
            currentCycle = newCycle
        }

        return calculateLoad(arr)
    }

    private fun tilt(arr: Array<Array<Char>>, dx: Int, dy: Int) {
        val placed = mutableSetOf<Pair<Int, Int>>()
        val rowIndices = when {
            dy < 0 -> arr.lastIndex downTo 0
            else -> arr.indices
        }
        val colIndices = when {
            dx < 0 -> arr[0].lastIndex downTo 0
            else -> arr[0].indices
        }
        for (row in rowIndices) {
            for (col in colIndices) {
                if (arr[row][col] == RoundStone && row to col !in placed) {
                    // find new place for stone at [row, col]
                    var endPlace = row to col
                    var curRow = row
                    var curCol = col
                    while (true) {
                        curRow += dy
                        curCol += dx
                        if (curRow !in rowIndices || curCol !in colIndices) break
                        when (arr[curRow][curCol]){
                            EmptySpace -> endPlace = curRow to curCol
                            BlockStone -> break
                        }
                    }
                    if (endPlace != row to col) {
                        arr[row][col] = EmptySpace
                        arr[endPlace.first][endPlace.second] = RoundStone
                        placed += endPlace
                    }
                }
            }
        }
    }

    private fun calculateLoad(arr: Array<Array<Char>>): Int {
        var load = 0
        for (row in arr.indices) {
            for (col in arr[row].indices) {
                if (arr[row][col] == RoundStone) {
                    load += arr.size - row
                }
            }
        }
        return load
    }
}
