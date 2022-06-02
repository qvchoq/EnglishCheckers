package com.coursework.englishcheckers


import com.coursework.englishcheckers.controller.GameActivity
import com.coursework.englishcheckers.model.BoardCell
import com.coursework.englishcheckers.model.Checker
import com.coursework.englishcheckers.model.Converter
import com.coursework.englishcheckers.model.Game
import com.coursework.englishcheckers.model.Game.Companion.needToBeatMap
import com.coursework.englishcheckers.model.Game.Companion.playerTurn
import com.coursework.englishcheckers.model.Game.Companion.winner
import com.coursework.englishcheckers.view.Board.Companion.board
import com.coursework.englishcheckers.view.Board.Companion.checkersOnBoard
import org.junit.Test

import org.junit.Assert.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class Test {

    private fun fillBoard(map: Map<String, Int>) {
        for ((key, value) in map) {
            board[key] = BoardCell(value, false)
            checkersOnBoard[key] = Checker(value, false, key)
        }
    }
    private fun clearBoard(list: List<String>) {
        for (i in list) {
            board.remove(i)
            checkersOnBoard.remove(i)
        }
        needToBeatMap.clear()
    }

    @Test
    fun testGetPotentialFurtherMoveForDefaultChecker() {

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
        assertEquals(Pair("d1", "d3"), Game().getPotentialFurtherMovesForDefaultChecker("c2",2))

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
        assertEquals(Pair("d7", "d0"), Game().getPotentialFurtherMovesForDefaultChecker("c8",2))

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
        assertEquals(Pair("e0", "e2"), Game().getPotentialFurtherMovesForDefaultChecker("f1",1))

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
        assertEquals(Pair("e6", "e8"), Game().getPotentialFurtherMovesForDefaultChecker("f7",1))

    }

    @Test
    fun testGetPossibleMovesForDefaultChecker() {

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
        assertEquals(true , Game().getPossibleMovesForDefaultChecker("f4", 1).isEmpty())
        clearBoard(listOf("f4", "e3", "e5", "d2", "d6"))

    }

    @Test
    fun testCheckDefaultCheckerNeedToBeat() {

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
        assertEquals(mapOf("f3" to listOf("d5")), needToBeatMap)
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
        assertEquals(mapOf("e3" to listOf("g5"), "e5" to listOf("g3")), needToBeatMap)
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
        assertEquals(mapOf("f4" to listOf("d2", "d6")), needToBeatMap)
        clearBoard(listOf("d2", "d6", "e3", "e5", "f4", "g3", "g5"))

    }

    @Test
    fun testGetPotentialFurtherMoveForQueen() {
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
    fun testGetPossibleMovesForQueen() {
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
    fun testCheckQueenNeedToBeat() {
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
        checkersOnBoard["c2"]?.setQueen(true)
        Game().checkQueenNeedToBeat(1)
        assertEquals(mapOf("c2" to listOf("e4")), needToBeatMap)
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
        checkersOnBoard["e4"]?.setQueen(true)
        Game().checkQueenNeedToBeat(2)
        assertEquals(mapOf("e4" to listOf("c2", "c6", "g2", "g6")), needToBeatMap)
        clearBoard(listOf("e4", "d3", "d5", "f3", "f5", "g2", "g6", "c2", "c6"))
    }

    @Test
    fun testGetCellBetweenTwo() {
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
    fun testEndGameWithWinnerByBeatingAllCheckers() {

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
    fun testEndGameWithWinnerByNoPossibleMoves() {
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
        needToBeatMap["g8"] = mutableListOf("e6")
        fillBoard(mapOf("g8" to 1, "f7" to 2, "e6" to 0))
        assertEquals(true, Game().endGameWithWinnerByNoPossibleMoves(2))
        clearBoard(listOf("g8", "f7", "e6"))
    }

    @Test
    fun testClearAllData() {
        winner = 2
        playerTurn = 2
        needToBeatMap["a1"] = mutableListOf("b2", "b4")
        board["a4"] = BoardCell(1, false)
        checkersOnBoard["a1"] = Checker(1, false, "a1")

        Game().clearAllData()

        assertEquals(0, winner)
        assertEquals(1, playerTurn)
        assertEquals(true, needToBeatMap.isEmpty())
        assertEquals(true, board.isEmpty())
        assertEquals(true, checkersOnBoard.isEmpty())
    }

    @Test
    fun testTouchOnChecker() {
        fillBoard(mapOf("a1" to 1))
        assertEquals(true, Game().touchOnChecker(30, 460))
        clearBoard(listOf("a1"))
    }

    @Test
    fun testTouchOnBoard() {
        assertEquals(true, Game().touchOnBoard(150, 800))
        assertEquals(false, Game().touchOnBoard(150, 100))
    }

    @Test
    fun testPlaceCellsOnBoard() {
        Game().placeCellsOnBoard()
        assertTrue(board["a1"] != null)
        assertTrue(board["a8"] != null)
        assertTrue(board["h1"] != null)
        assertTrue(board["h8"] != null)
    }

    @Test
    fun testCoordinateToCell() {

        assertEquals((30 to 450), Converter().coordinateToCell(140, 460))
        assertEquals((160 to 710), Converter().coordinateToCell(256, 780))
        assertEquals((680 to 710), Converter().coordinateToCell(765, 744))
        assertEquals((940 to 1360), Converter().coordinateToCell(957, 1401))

    }

    @Test
    fun testCoordinateToCellName() {

        assertEquals("a1", Converter().coordinateToCellName(30, 450))
        assertEquals("c2", Converter().coordinateToCellName(160, 710))
        assertEquals("c6", Converter().coordinateToCellName(680, 710))
        assertEquals("h8", Converter().coordinateToCellName(940, 1360))
    }

    @Test
    fun testCellNameToCoordinate() {

        assertEquals((30 to 450), Converter().cellNameToCoordinate("a1"))
        assertEquals((160 to 710), Converter().cellNameToCoordinate("c2"))
        assertEquals((680 to 710), Converter().cellNameToCoordinate("c6"))
        assertEquals((940 to 1360), Converter().cellNameToCoordinate("h8"))

    }

    @Test
    fun testCellNameSeparate() {

        assertEquals(('a' to 1), Converter().cellNameSeparate("a1"))
        assertEquals(('c' to 3), Converter().cellNameSeparate("c3"))
        assertEquals(('f' to 6), Converter().cellNameSeparate("f6"))
        assertEquals(('e' to 8), Converter().cellNameSeparate("e8"))

    }

    @Test
    fun testGetPosChecker() {
        fillBoard(mapOf("h8" to 1, "h2" to 1, "a1" to 2))

        assertEquals("h8", checkersOnBoard["h8"]?.getPos())
        assertEquals("h2", checkersOnBoard["h2"]?.getPos())
        assertEquals("a1", checkersOnBoard["a1"]?.getPos())
    }

    @Test
    fun testSetAndGetHighlightCell() {
        board["a1"] = BoardCell(1, false)

        board["a1"]?.setHighlight(true)

        assertTrue(board["a1"]?.getHighlightInfo() == true)

    }

}