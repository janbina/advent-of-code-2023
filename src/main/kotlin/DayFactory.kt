import solutions.*
import java.io.BufferedReader

fun createDay(day: Int, input: BufferedReader): Day<*, *> {
    return when (day) {
        1 -> Day01(input)
        2 -> Day02(input)
        3 -> Day03(input)
        4 -> Day04(input)
        5 -> Day05(input)
        6 -> Day06(input)
        7 -> Day07(input)
        8 -> Day08(input)
        9 -> Day09(input)
        10 -> Day10(input)
        11 -> Day11(input)
        12 -> Day12(input)
        13 -> Day13(input)
        14 -> Day14(input)
        15 -> Day15(input)
        16 -> Day16(input)
        17 -> Day17(input)
        18 -> Day18(input)
        19 -> Day19(input)
        20 -> Day20(input)
        21 -> Day21(input)
        22 -> Day22(input)
        23 -> Day23(input)
        24 -> Day24(input)
        25 -> Day25(input)
        else -> error("Day $day not yet implemented")
    }
}
