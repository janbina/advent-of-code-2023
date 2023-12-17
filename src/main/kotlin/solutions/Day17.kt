package solutions

import utils.Move
import utils.Point2D
import utils.aStarSearch
import java.io.BufferedReader

class Day17(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val map = inputReader.transformLines { it.map { c -> c.digitToInt() } }

    override fun solvePart1(): Int {
        return getLeastHeatCost(str8bound = 1..3)
    }

    override fun solvePart2(): Int {
        return getLeastHeatCost(str8bound = 4..10)
    }

    private operator fun List<List<Int>>.get(p: Point2D): Int? = getOrNull(p.y)?.getOrNull(p.x)

    private data class Node(
        val position: Point2D,
        val prevMove: Move?,
    )

    private fun getLeastHeatCost(str8bound: IntRange): Int {
        val start = Point2D(x = 0, y = 0)
        val end = Point2D(x = map[0].lastIndex, y = map.lastIndex)

        val path = aStarSearch(
            start = Node(start, null),
            next = { it.next(str8bound) },
            isEnd = { it.position == end },
            heuristicCostToEnd = { it.position.distanceTo(end) },
        ) ?: error("no path found")

        return path.cost
    }

    private fun Node.next(str8bound: IntRange): List<Pair<Node, Int>> {
        val moves = when (prevMove) {
            Move.left, Move.right -> listOf(Move.up, Move.down)
            Move.up, Move.down -> listOf(Move.left, Move.right)
            else -> listOf(Move.up, Move.down, Move.left, Move.right)
        }

        return buildList {
            for (move in moves) {
                var cost = 0
                var pos = position
                for (i in 1..str8bound.last) {
                    pos = pos.applyMove(move)
                    cost += map[pos] ?: break
                    if (i >= str8bound.first) {
                        add(Node(pos, move) to cost)
                    }
                }
            }
        }
    }
}
