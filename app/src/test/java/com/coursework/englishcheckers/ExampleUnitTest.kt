package com.coursework.englishcheckers


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