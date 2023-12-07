package solutions

import utils.splitOnWhitespace
import java.io.BufferedReader
import java.lang.IllegalArgumentException

class Day07(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input = inputReader.transformLines { CamelCardsHand.fromString(it) }

    override fun solvePart1(): Int {
        return getTotalWinnings(withJokers = false)
    }

    override fun solvePart2(): Int {
        return getTotalWinnings(withJokers = true)
    }

    private fun getTotalWinnings(withJokers: Boolean): Int {
        return input
            .sortedWith(CamelCardsHand.comparator(withJokers = withJokers))
            .withIndex()
            .sumOf { (index, hand) -> (index + 1) * hand.bid }
    }
}

private class CamelCardsHand private constructor(
    val hand: String,
    val bid: Int,
) {
    val type = hand.handType(withJokers = false)
    val jokerType = hand.handType(withJokers = true)

    fun getType(withJokers: Boolean): HandType {
        return if (withJokers) jokerType else type
    }

    override fun toString(): String {
        return "CamelCardsHand($hand, $bid, $type, $jokerType)"
    }

    companion object {
        fun comparator(withJokers: Boolean) = object : Comparator<CamelCardsHand> {
            override fun compare(o1: CamelCardsHand, o2: CamelCardsHand): Int {
                val s1 = o1.getType(withJokers).strength
                val s2 = o2.getType(withJokers).strength

                if (s1 != s2) return s1 - s2

                return o1.hand.zip(o2.hand)
                    .map { it.first.strength(withJokers) - it.second.strength(withJokers) }
                    .firstOrNull { it != 0 }
                    ?: 0
            }
        }

        fun fromString(string: String): CamelCardsHand {
            val (hand, bid) = string.splitOnWhitespace()
            require(hand.length == 5)
            hand.forEach { it.strength(false) } // ensures we only have valid cards
            return CamelCardsHand(hand, bid.toInt())
        }
    }

    enum class HandType(
        val strength: Int,
    ) {
        FiveAKind(6),
        FourAKind(5),
        FullHouse(4),
        ThreeAKind(3),
        TwoPair(2),
        OnePair(1),
        HighCard(0),
    }
}

private fun Char.strength(withJokers: Boolean): Int {
    return when {
        this.isDigit() -> this.digitToInt()
        this == 'T' -> 10
        this == 'J' -> if (withJokers) -1 else 11
        this == 'Q' -> 12
        this == 'K' -> 13
        this == 'A' -> 14
        else -> throw IllegalArgumentException("not a card symbol: $this")
    }
}

private fun String.handType(withJokers: Boolean): CamelCardsHand.HandType {
    require(this.length == 5)
    val counts = if (withJokers) {
        groupingBy { it }.eachCount().toMutableMap().also { map ->
            val jokersCount = map['J'] ?: 0
            if (jokersCount != 5) {
                map.remove('J')
                map.maxByOrNull { it.value }?.let { (k, v) ->
                    map[k] = v + jokersCount
                }
            }
        }
    } else {
        groupingBy { it }.eachCount()
    }

    return when {
        counts.any { it.value == 5 } -> CamelCardsHand.HandType.FiveAKind
        counts.any { it.value == 4 } -> CamelCardsHand.HandType.FourAKind
        counts.any { it.value == 3 } && counts.any { it.value == 2 } -> CamelCardsHand.HandType.FullHouse
        counts.any { it.value == 3 } -> CamelCardsHand.HandType.ThreeAKind
        counts.count { it.value == 2 } == 2 -> CamelCardsHand.HandType.TwoPair
        counts.any { it.value == 2 } -> CamelCardsHand.HandType.OnePair
        else -> CamelCardsHand.HandType.HighCard
    }
}
