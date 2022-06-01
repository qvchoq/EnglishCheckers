package com.coursework.englishcheckers

import com.coursework.englishcheckers.model.BoardCell
import com.coursework.englishcheckers.model.Checker
import com.coursework.englishcheckers.model.Game
import com.coursework.englishcheckers.model.Game.Companion.needToBeatMap
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
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
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
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
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
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
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
         *  e   [0, 0, 0, 0, 0, 0, 0, 0]
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
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
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
         *  e   [0, 2, 0, 0, 0, 0, 0, 0]
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
         *  d   [2, 0, 0, 0, 0, 0, 0, 0]
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
         *  g   [0, 0, 0, 0, 0, 0, 0, 0]
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
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
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
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
         *  c   [0, 1, 0, 0, 0, 0, 0, 0]
         *  d   [0, 0, 0, 0, 0, 0, 0, 0]
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
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
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
         *  f   [0, 0, 0, 0, 0, 0, 0, 0]
         *  g   [0, 0, 0, 0, 0, 0, 0, 1]
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("g8" to 1, "h7" to 0, "f7" to 0))
        assertEquals(listOf("f7", "h7"), Game().getPotentialFurtherMovesForQueen("g8"))
        clearBoard(listOf("g8", "h7", "f7"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 1, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
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
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
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
         *  h   [0, 0, 0, 0, 0, 0, 0, 0]
         *
         */
        fillBoard(mapOf("g8" to 1, "h7" to 0, "f7" to 2))
        assertEquals(listOf("h7"), Game().getPossibleMovesForQueen("g8"))
        clearBoard(listOf("g8", "h7", "f7"))

        /* Example:
         *       1  2  3  4  5  6  7  8
         *
         *  a   [0, 0, 0, 1, 0, 0, 0, 0]
         *  b   [0, 0, 0, 0, 0, 0, 0, 0]
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

    }
}