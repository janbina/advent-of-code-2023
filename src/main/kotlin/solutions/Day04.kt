package solutions

import utils.splitOnWhitespace
import utils.sumOf
import java.io.BufferedReader
import kotlin.math.pow

class Day04(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input = inputReader.transformLines { Game.fromString(it) }

    override fun solvePart1(): Int {
        return input.sumOf { it.points() }
    }

    override fun solvePart2(): Int {
        val numOfScratchCards = mutableMapOf<Int, Int>()
        input.forEach { numOfScratchCards[it.id] = 1 }
        input.forEach { game ->
            val numCards = numOfScratchCards[game.id]
            val winNums = game.myNums.count { it in game.winningNums }
            for (i in game.id + 1..game.id + winNums) {
                if (i in numOfScratchCards) {
                    numOfScratchCards[i] = numOfScratchCards[i]!! + numCards!!
                }
            }
        }


        return numOfScratchCards.sumOf { it.value }
    }

    private class Game(
        val id: Int,
        val winningNums: List<Int>,
        val myNums: List<Int>,
    ) {

        fun points(): Int {
            val count = myNums.count { it in winningNums }
            if (count == 0) return 0
            return 2.toFloat().pow(count - 1).toInt()
        }

        companion object {
            fun fromString(string: String): Game {
                val (game, win, my) = string.split(":", "|")
                return Game(
                    id = game.filter { it.isDigit() }.toInt(),
                    winningNums = win.splitOnWhitespace()
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                        .map { it.toInt() },
                    myNums = my.splitOnWhitespace()
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                        .map { it.toInt() },
                )
            }
        }
    }
}
