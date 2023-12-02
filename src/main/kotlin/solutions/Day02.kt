package solutions

import utils.product
import java.io.BufferedReader

class Day02(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val games = inputReader.transformLines { Game.fromString(it) }

    override fun solvePart1(): Int {
        val max = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14,
        )

        fun Game.isPossible(): Boolean {
            return sets
                .flatMap { it.entries }
                .all { (color, num) ->
                    num <= max.getOrDefault(color, Int.MAX_VALUE)
                }
        }

        return games.filter { it.isPossible() }.sumOf { it.id }
    }

    override fun solvePart2(): Int {
        fun Game.power(): Int {
            val merged = sets.reduce { acc, map ->
                acc.toMutableMap().apply {
                    map.forEach { (color, num) ->
                        merge(color, num, ::maxOf)
                    }
                }
            }
            return merged.values.product().toInt()
        }

        return games.sumOf { it.power() }
    }
}

private data class Game(
    val id: Int,
    val sets: List<Set>,
) {

    companion object {
        fun fromString(string: String): Game {
            val (name, rest) = string.split(":")
            val id = name.filter { it.isDigit() }.toInt()
            val sets = rest.split(";").map { set ->
                set.split(",").associate {
                    val (num, color) = it.trim().split(" ")
                    color to num.toInt()
                }
            }
            return Game(id, sets)
        }
    }
}

private typealias Set = Map<String, Int>
