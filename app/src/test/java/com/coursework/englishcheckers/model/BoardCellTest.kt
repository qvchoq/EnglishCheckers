package com.coursework.englishcheckers.model

import com.coursework.englishcheckers.view.Board
import org.junit.Test

import org.junit.Assert.*

class BoardCellTest {

    @Test
    fun setAndGetHighlightCell() {

        Board.board["a1"] = BoardCell(1, false)

        Board.board["a1"]?.setHighlight(true)

        assertTrue(Board.board["a1"]?.getHighlightInfo() == true)

    }
}