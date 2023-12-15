package solutions

import java.io.BufferedReader

class Day15(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val instructions = inputReader.transformLines { it }.first().split(",")

    override fun solvePart1(): Int {
        return instructions.sumOf { it.hash() }
    }

    override fun solvePart2(): Int {
        val boxes = Array<MutableList<Pair<String, Int>>>(256) { mutableListOf() }

        for (ins in instructions) {
            if ('=' in ins) {
                val (id, focal) = ins.split('=')
                val box = boxes[id.hash()]
                val index = box.indexOfFirst { it.first == id }
                if (index >= 0) {
                    box[index] = id to focal.toInt()
                } else {
                    box.add(id to focal.toInt())
                }
            } else {
                val id = ins.dropLast(1)
                boxes[id.hash()].removeIf { it.first == id }
            }
        }

        return boxes.withIndex().sumOf { (index, box) ->
            (index + 1) * box.withIndex().sumOf { (i, lens) -> (i + 1) * lens.second }
        }
    }

    private fun String.hash(): Int {
        var h = 0
        for (c in this) h = (h + c.code) * 17 % 256
        return h
    }
}
