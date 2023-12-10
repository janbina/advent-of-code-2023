package solutions

import utils.Move
import utils.Point2D
import utils.getOrErr
import java.io.BufferedReader

class Day10(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val map: Map<Point2D, Char> by lazy {
        buildMap {
            inputReader.transformLines { it }.forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    this[Point2D(x, y)] = c
                }
            }
        }
    }
    private val start = map.filterValues { it == 'S' }.keys.first()

    private fun createPath(): List<Pair<Point2D, Move>> {
        return buildList {
            var (p, d) = listOf(Move.up, Move.down, Move.left, Move.right).firstNotNullOf { move ->
                runCatching {
                    getNext(start, move).also {
                        add(start to move)
                    }
                }.getOrNull()
            }
            while (p != start) {
                add(p to d)
                val next = getNext(p, d)
                p = next.first
                d = next.second
            }
        }
    }

    override fun solvePart1(): Int {
        val path = createPath()
        return path.size / 2
    }

    override fun solvePart2(): Int {
        val path = createPath()

        return runCatching { getInside(path, false) }
            .getOrNull() ?: getInside(path, true)
    }

    private fun getInside(path: List<Pair<Point2D, Move>>, toTheLeft: Boolean): Int {
        val set = mutableSetOf<Point2D>()
        val pathSet = path.map { it.first }.toSet()
        path.forEachIndexed { index, (p, d) ->
            val dir = path.getOrNull(index - 1)?.second ?: d
            val moves = getMoves(p, dir, toTheLeft)
            moves.forEach { move ->
                var c = p
                while (true) {
                    c = c.applyMove(move)
                    if (c !in map) error("!!!")
                    if (c in pathSet) break
                    set += c
                }
            }
        }
        return set.size
    }

    private fun getMoves(p: Point2D, dir: Move, toTheLeft: Boolean): List<Move> {
        val s = map.getOrErr(p)
        return when {
            s == 'S' && dir == Move.up -> listOf(Move.left)
            s == 'S' && dir == Move.down -> listOf(Move.right)
            s == 'S' && dir == Move.left -> listOf(Move.down)
            s == 'S' && dir == Move.right -> listOf(Move.up)
            s == '|' && dir == Move.up -> listOf(Move.left)
            s == '|' && dir == Move.down -> listOf(Move.right)
            s == '-' && dir == Move.left -> listOf(Move.down)
            s == '-' && dir == Move.right -> listOf(Move.up)
            s == 'L' && dir == Move.down -> listOf(Move.up, Move.right)
            s == 'L' && dir == Move.left -> listOf(Move.down, Move.left)
            s == 'J' && dir == Move.right -> listOf(Move.up, Move.left)
            s == 'J' && dir == Move.down -> listOf(Move.down, Move.right)
            s == 'F' && dir == Move.up -> listOf(Move.up, Move.left)
            s == 'F' && dir == Move.left -> listOf(Move.down, Move.right)
            s == '7' && dir == Move.right -> listOf(Move.up, Move.right)
            s == '7' && dir == Move.up -> listOf(Move.down, Move.left)
            else -> error("")
        }.run {
            if (toTheLeft) this else this.map { it.inverted() }
        }
    }

    private fun getNext(p: Point2D, dir: Move): Pair<Point2D, Move> {
        val np = p.applyMove(dir)
        val s = map.getOrErr(np)
        return np to when {
            s == 'S' -> dir
            s == '|' && dir == Move.up -> Move.up
            s == '|' && dir == Move.down -> Move.down
            s == '-' && dir == Move.left -> Move.left
            s == '-' && dir == Move.right -> Move.right
            s == 'L' && dir == Move.down -> Move.right
            s == 'L' && dir == Move.left -> Move.up
            s == 'J' && dir == Move.right -> Move.up
            s == 'J' && dir == Move.down -> Move.left
            s == 'F' && dir == Move.up -> Move.right
            s == 'F' && dir == Move.left -> Move.down
            s == '7' && dir == Move.right -> Move.down
            s == '7' && dir == Move.up -> Move.left
            else -> error("!!")
        }
    }
}
