package com.coursework.englishcheckers.model

import com.coursework.englishcheckers.view.Board
import org.junit.Test

import org.junit.Assert.*

class CheckerTest {

    private fun fillBoard(map: Map<String, Int>) {
        for ((key, value) in map) {
            Board.board[key] = BoardCell(value, false)
            Board.checkersOnBoard[key] = Checker(value, false, key)
        }
    }

    @Test
    fun getPos() {
        fillBoard(mapOf("h8" to 1, "h2" to 1, "a1" to 2))

        assertEquals("h8", Board.checkersOnBoard["h8"]?.getPos())
        assertEquals("h2", Board.checkersOnBoard["h2"]?.getPos())
        assertEquals("a1", Board.checkersOnBoard["a1"]?.getPos())
    }
}