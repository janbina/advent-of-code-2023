package solutions

import utils.Move
import utils.Point2D
import java.io.BufferedReader

class Day16(
    inputReader: BufferedReader,
) : Day<Any, Any>() {

    private val input = inputReader.transformLines { it }

    private data class Beam(
        val position: Point2D,
        val direction: Move,
    )

    override fun solvePart1(): Any {
        return getNumberOfEnergizedTiles(Beam(Point2D(0, 0), Move.right))
    }

    override fun solvePart2(): Any {
        val startPositions = input.indices.map { y ->
            Beam(position = Point2D(x = 0, y = y), direction = Move.right)
        } + input.indices.map { y ->
            Beam(position = Point2D(x = input[0].lastIndex, y = y), direction = Move.left)
        } + input[0].indices.map { x ->
            Beam(position = Point2D(x = x, y = 0), direction = Move.down)
        } + input[0].indices.map { x ->
            Beam(position = Point2D(x = x, y = input.lastIndex), direction = Move.up)
        }

        return startPositions.maxOf { getNumberOfEnergizedTiles(it) }
    }

    private fun getNumberOfEnergizedTiles(start: Beam): Int {
        var beams = listOf(start)
        val beamSet = mutableSetOf<Beam>()
        val energized = mutableSetOf(start.position)

        while (beams.isNotEmpty()) {
            beams = beams.flatMap { it.moveNext() }.filter { it !in beamSet }
            beamSet += beams
            energized += beams.map { it.position }
        }

        return energized.size
    }

    private fun Beam.moveNext(): List<Beam> {
        val char = input.getOrNull(position.y)?.getOrNull(position.x)

        fun withMove(move: Move) = copy(position = position.applyMove(move), direction = move)

        return when (char) {
            '.' -> listOf(withMove(direction))
            '/' -> when (direction) {
                Move.up -> listOf(withMove(Move.right))
                Move.down -> listOf(withMove(Move.left))
                Move.right -> listOf(withMove(Move.up))
                Move.left -> listOf(withMove(Move.down))
                else -> error("Invalid direction")
            }

            '\\' -> when (direction) {
                Move.up -> listOf(withMove(Move.left))
                Move.down -> listOf(withMove(Move.right))
                Move.right -> listOf(withMove(Move.down))
                Move.left -> listOf(withMove(Move.up))
                else -> error("Invalid direction")
            }

            '|' -> when (direction) {
                Move.up,
                Move.down -> listOf(withMove(direction))

                Move.right,
                Move.left -> listOf(withMove(Move.up), withMove(Move.down))

                else -> error("Invalid direction")
            }

            '-' -> when (direction) {
                Move.up,
                Move.down -> listOf(withMove(Move.left), withMove(Move.right))

                Move.right,
                Move.left -> listOf(withMove(direction))

                else -> error("Invalid direction")
            }

            null -> error("Out of map")
            else -> error("invalid char: $char")
        }.filter { it.position.y in input.indices && it.position.x in input[0].indices }
    }
}
