package solutions

import utils.split
import java.io.BufferedReader

class Day13(
    inputReader: BufferedReader,
) : Day<Any, Any>() {

    private val input = inputReader.transformLines { it }.split { it.isBlank() }

    override fun solvePart1(): Any {
        return input.sumOf { it.summarize(withSmudge = false) }
    }

    override fun solvePart2(): Any {
        return input.sumOf { it.summarize(withSmudge = true) }
    }

    private fun List<String>.summarize(withSmudge: Boolean): Int {
        for (i in 0..<first().lastIndex) {
            if (isPerfectColumnReflection(i, withSmudge)) {
                return i + 1
            }
        }

        for (i in 0..<lastIndex) {
            if (isPerfectRowReflection(i, withSmudge)) {
                return (i + 1) * 100
            }
        }

        return 0
    }

    private fun List<String>.isPerfectColumnReflection(i: Int, withSmudge: Boolean): Boolean {
        var smudgeCorrected = false
        var inc = 0
        while (true) {
            val a = i - inc
            val b = i + 1 + inc
            inc++
            if (a !in get(0).indices || b !in get(0).indices) {
                if (withSmudge) return smudgeCorrected
                else return true
            }
            val diff = count { it[a] != it[b] }
            if (diff == 1 && withSmudge && !smudgeCorrected) {
                smudgeCorrected = true
            } else if (diff != 0) {
                return false
            }
        }
    }

    private fun List<String>.isPerfectRowReflection(i: Int, withSmudge: Boolean): Boolean {
        var smudgeCorrected = false
        var inc = 0
        while (true) {
            val a = i - inc
            val b = i + 1 + inc
            inc++
            if (a !in indices || b !in indices) {
                if (withSmudge) return smudgeCorrected
                else return true
            }
            val diff = get(a).zip(get(b)).count { it.first != it.second }
            if (diff == 1 && withSmudge && !smudgeCorrected) {
                smudgeCorrected = true
            } else if (diff != 0) {
                return false
            }
        }
    }
}
