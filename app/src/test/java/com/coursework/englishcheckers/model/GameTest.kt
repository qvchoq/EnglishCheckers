package com.coursework.englishcheckers.model

import com.coursework.englishcheckers.view.Board
import org.junit.Test

import org.junit.Assert.*

class GameTest {

    private fun fillBoard(map: Map<String, Int>) {
        for ((key, value) in map) {
            Board.board[key] = BoardCell(value, false)
            Board.checkersOnBoard[key] = Checker(value, false, key)
        }
    }

    private fun clearBoard(list: List<String>) {
        for (i in list) {
            Board.board.remove(i)
            Board.checkersOnBoard.remove(i)
        }
        Game.needToBeatMap.clear()
    }

    @Test
    fun getPotentialFurtherMovesForDefaultChecker() {

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 2, 0, 0, 0, 0, 0, 0]
         *  d   [x, 0, x, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        assertEquals(Pair("d1", "d3"), Game().getPotentialFurtherMovesForDefaultChecker("c2", 2))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 2]
         *  d   [0, 0, 0, 0, 0, 0, x, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        assertEquals(Pair("d7", "d0"), Game().getPotentialFurtherMovesForDefaultChecker("c8", 2))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, x, 0, 0, 0, 0, 0, 0]
         *  f   [1, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        assertEquals(Pair("e0", "e2"), Game().getPotentialFurtherMovesForDefaultChecker("f1", 1))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, x, 0, x]
         *  f   [0, 0, 0, 0, 0, 0, 1, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        assertEquals(Pair("e6", "e8"), Game().getPotentialFurtherMovesForDefaultChecker("f7", 1))

    }

    @Test
    fun getPossibleMovesForDefaultChecker() {

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 2, 0, 0, 0, 0]
         *  d   [0, 0, x, 0, x, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("c4" to 2, "d3" to 0, "d5" to 0))
        assertEquals(listOf("d3", "d5"), Game().getPossibleMovesForDefaultChecker("c4", 2))
        clearBoard(listOf("c4", "d3", "d5"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [2, 0, 0, 0, 2, 0, 0, 0]
         *  e   [0, 2, 0, x, 0, 0, 0, 0]
         *  f   [0, 0, 1, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("e2" to 2, "d1" to 2, "d5" to 2, "e4" to 0))
        assertEquals(listOf("e4"), Game().getPossibleMovesForDefaultChecker("f3", 1))
        clearBoard(listOf("e2", "d1", "d5", "e4"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 2, 0, 0, 0, 2, 0, 0]
         *  e   [0, 0, 2, 0, 2, 0, 0, 0]
         *  f   [0, 0, 0, 1, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("f4" to 1, "e3" to 2, "e5" to 2, "d2" to 2, "d6" to 2))
        assertEquals(true, Game().getPossibleMovesForDefaultChecker("f4", 1).isEmpty())
        clearBoard(listOf("f4", "e3", "e5", "d2", "d6"))

    }

    @Test
    fun checkDefaultCheckerNeedToBeat() {

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [2, 0, 0, 0, x, 0, 0, 0]
         *  e   [0, 2, 0, 2, 0, 0, 0, 0]
         *  f   [0, 0, 1, 0, 0, 0, 0, 0]
         *  g   [0, 1, 0, 1, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("e2" to 2, "d1" to 2, "e4" to 2, "d5" to 0, "f3" to 1, "g2" to 1, "g4" to 1))
        Game().checkDefaultCheckerNeedToBeat(1)
        assertEquals(mapOf("f3" to listOf("d5")), Game.needToBeatMap)
        clearBoard(listOf("e2", "d1", "d5", "e4"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 2, 0, 0, 0, 2, 0, 0]
         *  e   [0, 0, 2, 0, 2, 0, 0, 0]
         *  f   [0, 0, 0, 1, 0, 0, 0, 0]
         *  g   [0, 0, x, 0, x, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("d2" to 2, "d6" to 2, "e3" to 2, "e5" to 2, "f4" to 1, "g3" to 0, "g5" to 0))
        Game().checkDefaultCheckerNeedToBeat(2)
        assertEquals(mapOf("e3" to listOf("g5"), "e5" to listOf("g3")), Game.needToBeatMap)
        clearBoard(listOf("d2", "d6", "e3", "e5", "f4", "g3", "g5"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, x, 0, 0, 0, x, 0, 0]
         *  e   [0, 0, 2, 0, 2, 0, 0, 0]
         *  f   [0, 0, 0, 1, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("d2" to 0, "d6" to 0, "e3" to 2, "e5" to 2, "f4" to 1, "g3" to 0, "g5" to 0))
        Game().checkDefaultCheckerNeedToBeat(1)
        assertEquals(mapOf("f4" to listOf("d2", "d6")), Game.needToBeatMap)
        clearBoard(listOf("d2", "d6", "e3", "e5", "f4", "g3", "g5"))

    }

    @Test
    fun getPotentialFurtherMovesForQueen() {
        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [x, 0, x, 0, 0, 0, 0, 0]
         *  c   [0, 1, 0, 0, 0, 0, 0, 0]
         *  d   [x, 0, x, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("c2" to 1,"b1" to 0, "b3" to 0, "d1" to 0, "d3" to 0))
        assertEquals(listOf("b1", "b3", "d1", "d3"), Game().getPotentialFurtherMovesForQueen("c2"))
        clearBoard(listOf("c2", "b1", "b3", "d1", "d3"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 1]
         *  b   [0, 0, 0, 0, 0, 0, x, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("a8" to 1, "b7" to 0))
        assertEquals(listOf("b7"), Game().getPotentialFurtherMovesForQueen("a8"))
        clearBoard(listOf("a8", "b7"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, x, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 1]
         *  h   [0, 0, 0, 0, 0, 0, x, 0]
         *
         */
        fillBoard(mapOf("g8" to 1, "h7" to 0, "f7" to 0))
        assertEquals(listOf("f7", "h7"), Game().getPotentialFurtherMovesForQueen("g8"))
        clearBoard(listOf("g8", "h7", "f7"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 1, 0, 0, 0, 0]
         *  b   [0, 0, x, 0, x, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("a4" to 1, "b3" to 0, "b5" to 0))
        assertEquals(listOf("b3", "b5"), Game().getPotentialFurtherMovesForQueen("a4"))
        clearBoard(listOf("a4", "b3", "b5"))
    }

    @Test
    fun getPossibleMovesForQueen() {
        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [x, 0, x, 0, 0, 0, 0, 0]
         *  c   [0, 1, 0, 0, 0, 0, 0, 0]
         *  d   [2, 0, 2, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("c2" to 1,"b1" to 0, "b3" to 0, "d1" to 2, "d3" to 2))
        assertEquals(listOf("b1", "b3"), Game().getPossibleMovesForQueen("c2"))
        clearBoard(listOf("c2", "b1", "b3", "d1", "d3"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 1]
         *  b   [0, 0, 0, 0, 0, 0, 2, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("a8" to 1, "b7" to 2))
        assertEquals(true, Game().getPossibleMovesForQueen("a8").isEmpty())
        clearBoard(listOf("a8", "b7"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 2, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 1]
         *  h   [0, 0, 0, 0, 0, 0, x, 0]
         *
         */
        fillBoard(mapOf("g8" to 1, "h7" to 0, "f7" to 2))
        assertEquals(listOf("h7"), Game().getPossibleMovesForQueen("g8"))
        clearBoard(listOf("g8", "h7", "f7"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 1, 0, 0, 0, 0]
         *  b   [0, 0, x, 0, x, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("a4" to 1, "b3" to 0, "b5" to 0))
        assertEquals(listOf("b3", "b5"), Game().getPossibleMovesForQueen("a4"))
        clearBoard(listOf("a4", "b3", "b5"))
    }

    @Test
    fun checkQueenNeedToBeat() {
        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 1, 0, 0, 0, 0, 0, 0]
         *  d   [2, 0, 2, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, x, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("c2" to 1,"b1" to 0, "b3" to 0, "d1" to 2, "d3" to 2, "e4" to 0))
        Board.checkersOnBoard["c2"]?.setQueen(true)
        Game().checkQueenNeedToBeat(1)
        assertEquals(mapOf("c2" to listOf("e4")), Game.needToBeatMap)
        clearBoard(listOf("c2", "b1", "b3", "d1", "d3", "e4"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, x, 0, 0, 0, x, 0, 0]
         *  d   [0, 0, 1, 0, 1, 0, 0, 0]
         *  e   [0, 0, 0, 2, 0, 0, 0, 0]
         *  f   [0, 0, 1, 0, 1, 0, 0, 0]
         *  g   [0, x, 0, 0, 0, x, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("e4" to 2, "d3" to 1, "d5" to 1, "f3" to 1, "f5" to 1, "g2" to 0, "g6" to 0, "c2" to 0, "c6" to 0))
        Board.checkersOnBoard["e4"]?.setQueen(true)
        Game().checkQueenNeedToBeat(2)
        assertEquals(mapOf("e4" to listOf("c2", "c6", "g2", "g6")), Game.needToBeatMap)
        clearBoard(listOf("e4", "d3", "d5", "f3", "f5", "g2", "g6", "c2", "c6"))
    }

    @Test
    fun getCellBetweenTwo() {
        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [1, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, x, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 1, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        assertEquals("b2", Game().getCellBetweenTwo("a1", "c3"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 1, 0, 0, 0]
         *  g   [0, 0, 0, x, 0, 0, 0, 0]
         *  h   [0, 0, 1, 0, 0, 0, 0, 0]
         *
         */
        assertEquals("g4", Game().getCellBetweenTwo("h3", "f5"))
    }

    @Test
    fun touchOnChecker() {
        fillBoard(mapOf("a1" to 1))
        assertEquals(true, Game().touchOnChecker(30, 460))
        clearBoard(listOf("a1"))
    }

    @Test
    fun touchOnBoard() {
        assertEquals(true, Game().touchOnBoard(150, 800))
        assertEquals(false, Game().touchOnBoard(150, 100))
    }

    @Test
    fun changeCellColor() {
    }

    @Test
    fun placeCellsOnBoard() {
        Game().placeCellsOnBoard()
        assertTrue(Board.board["a1"] != null)
        assertTrue(Board.board["a8"] != null)
        assertTrue(Board.board["h1"] != null)
        assertTrue(Board.board["h8"] != null)
    }

    @Test
    fun endGameWithWinnerByBeatingAllCheckers() {

        fillBoard(mapOf("c2" to 1,"b1" to 2))
        assertEquals(false, Game().endGameWithWinnerByBeatingAllCheckers())

        fillBoard(mapOf("c2" to 0,"b1" to 0))
        assertEquals(false, Game().endGameWithWinnerByBeatingAllCheckers())

        fillBoard(mapOf("c2" to 1,"b1" to 0))
        assertEquals(true, Game().endGameWithWinnerByBeatingAllCheckers())

        fillBoard(mapOf("c2" to 2,"b1" to 0))
        assertEquals(true, Game().endGameWithWinnerByBeatingAllCheckers())
    }

    @Test
    fun endGameWithWinnerByNoPossibleMoves() {
        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 2, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 2, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 1]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("g8" to 1, "f7" to 2, "e6" to 2))
        assertEquals(true, Game().endGameWithWinnerByNoPossibleMoves(1))
        clearBoard(listOf("g8", "f7", "e6"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 2, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 1]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("g8" to 1, "f7" to 0, "e6" to 2))
        assertEquals(false, Game().endGameWithWinnerByNoPossibleMoves(1))
        clearBoard(listOf("g8", "f7", "e6"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 0, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 0, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
         *  f   [0, 0, 0, 0, 0, 0, 2, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 1]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        Game.needToBeatMap["g8"] = mutableListOf("e6")
        fillBoard(mapOf("g8" to 1, "f7" to 2, "e6" to 0))
        assertEquals(true, Game().endGameWithWinnerByNoPossibleMoves(2))
        clearBoard(listOf("g8", "f7", "e6"))
    }

    @Test
    fun clearAllData() {
        Game.winner = 2
        Game.playerTurn = 2
        Game.needToBeatMap["a1"] = mutableListOf("b2", "b4")
        Board.board["a4"] = BoardCell(1, false)
        Board.checkersOnBoard["a1"] = Checker(1, false, "a1")

        Game().clearAllData()

        assertEquals(0, Game.winner)
        assertEquals(1, Game.playerTurn)
        assertEquals(true, Game.needToBeatMap.isEmpty())
        assertEquals(true, Board.board.isEmpty())
        assertEquals(true, Board.checkersOnBoard.isEmpty())
    }
}